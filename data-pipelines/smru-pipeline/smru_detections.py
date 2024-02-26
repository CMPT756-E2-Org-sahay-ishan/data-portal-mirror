import requests
import json
import os.path
import time
import datetime
import psycopg2
import logging
import pytz
# Logging setup
logging.basicConfig(filename='sample_file.log', level=logging.ERROR)

# Connect to the database
def connect_to_database():
    return psycopg2.connect(
        database="",
        user='',
        password='USE_YOUR_OWN_PASSWORD',
        host='localhost',
        port='0'
    )

##################################
# time zone as UTC
desired_timezone = pytz.utc
# Record the current time
##################################### Handling start and end time for the query
def get_start_time(start_time_file):
    if os.path.exists(start_time_file):
        with open(start_time_file, 'r') as file:
            start_time_data = json.load(file)
            current= datetime.datetime.strptime(start_time_data['start_time'], '%Y-%m-%dT%H:%M:%S%z').replace(tzinfo=desired_timezone)
            update_start_time(start_time_file, datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone))
            return current
    else:
        # File doesn't exist, create it and set the start time to 6 hours ago in UTC
        current_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
        current_time = current_time - datetime.timedelta(hours=6)
        update_start_time(start_time_file, current_time)
        return current_time

def update_start_time(start_time_file, current_time):
    start_time_data = {'start_time': current_time.strftime('%Y-%m-%dT%H:%M:%S%z')}
    with open(start_time_file, 'w') as file:
        json.dump(start_time_data, file)

def get_start_and_end_times(start_time_file):
    # Fetch the stored start time 
    stored_start_time = get_start_time(start_time_file)
    stored_end_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
    
    return stored_start_time,stored_end_time


# Read the latest time and date that API fetch happened
file_path = 'sample_file.json'
start_time, end_time =  get_start_and_end_times(file_path)
start_timestamp_milliseconds = int(start_time.timestamp() * 1000)
end_timestamp_milliseconds = int(end_time.timestamp() * 1000)
print(start_timestamp_milliseconds)
print(end_timestamp_milliseconds)


# Create the API URL
api_url = "api_url".format(
    start_timestamp_milliseconds, end_timestamp_milliseconds)

# API call
try:
    response = requests.get(api_url, timeout=20)
    detections = response.json()
    conn = connect_to_database()
    cursor = conn.cursor()
    
    for detection in detections:
        if 'eventAnnotations' in detection:
            del detection['eventAnnotations']
        columns = ', '.join(str(x) for x in detection.keys())
        values = ', '.join("'" + str(x) + "'" for x in detection.values())
        
        ##Insert the events details into a table that holds the latest events 
        sql_latest_events = "INSERT INTO %s ( %s ) VALUES ( %s );" % ('smru_limekiln_latest_detection_events', columns, values)
             
        try:
            cursor.execute(sql_latest_events)
        except psycopg2.Error as err:
            logging.error("Error in executing SQL statement: %s", str(err))
            conn.rollback()
        ##Insert latest events details in to a table that holds events that their annotaions are not ready yet
        sql_events_with_no_annotation = "INSERT INTO smru_pending_annotations (alert_type, start_time, end_time, event_id, recording_id, densest_minute_start_time, has_annotation) VALUES ('{}', '{}', '{}', '{}', '{}', '{}', {})".format(
        detection['alertType'], detection['startTime'], detection['endTime'], 
        detection['idString'], detection['recordingIdString'], detection['densestMinuteStartTime'], 
        detection['hasAnnotation'])

        try:
            cursor.execute(sql_events_with_no_annotation)
        except psycopg2.Error as err:
            logging.error("Error in executing SQL statement: %s", str(err))
            conn.rollback()


except requests.exceptions.HTTPError as errh:
    logging.error("Error in fetching SMRU detections events: %s", errh)
except requests.exceptions.ConnectionError as errc:
    logging.error("Error in fetching SMRU detections events: %s", errc)
    print("Error Connecting:", errc)
except requests.exceptions.Timeout as errt:
    logging.error("Error in fetching SMRU detections events: %s", errt)
    print("Timeout Error:", errt)
except requests.exceptions.RequestException as err:
    logging.error("Error in fetching SMRU detections events: %s", err)
    print("OOps: Something Else", err)

finally:
    conn.commit()
    cursor.close()
    conn.close()
