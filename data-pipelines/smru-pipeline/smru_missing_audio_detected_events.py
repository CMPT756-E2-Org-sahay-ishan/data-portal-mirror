import requests
import json
import os.path
import time
import datetime
import psycopg2
import urllib
import shutil
import logging
from urllib.request import urlopen
from shutil import copyfileobj
from datetime import datetime, timedelta
from pipeline_env import load_env, load_latest

#########################################################
def is_less_than_one_day(timestamp_str):
    try:
        # Parse the timestamp string into a datetime object
        timestamp = datetime.strptime(timestamp_str, "%Y-%m-%dT%H:%M:%S.%fZ")
        
        # Get the current time
        current_time = datetime.utcnow()  # Assuming the timestamp is in UTC
        
        # Calculate the difference between the current time and the timestamp
        time_difference = current_time - timestamp
        
        # Define a timedelta representing one day
        one_day = timedelta(days=1)
        
        # Check if the difference is less than one day
        return time_difference < one_day
    except ValueError:
        # Handle parsing errors (e.g., invalid timestamp format)
        print("Error: Invalid timestamp format")
        return False
##########################################################

# Function to return recording ID
def fetch_recording_id(event_id, event_data):

        for event in event_data:
            
            if event['id'] == event_id:
                
                return event['recordingId']
        return None

##############################################################

#Function to process records
def process_records(records, cursor, event_data):
    for record in records:
        event_id = record[0]
        recording_id = record[1]
        if record[1]=='None':  # Check if recording_id is missing
            recording_id = fetch_recording_id(event_id, event_data)  # Attempt to fetch recording ID
            if recording_id is not None:  # If recording ID is fetched successfully
                try:
                    cursor.execute("UPDATE smru_limekiln_latest_detection_events SET recordingidstring = %s WHERE idstring = %s", (recording_id, event_id))
                    
                except Exception as err:
                    logging.error(f"Error updating recording ID for event {event_id}: {err}")
            else:
                if is_less_than_one_day(record[2]):  ## We check if an event after a day still has None recoding id, we remove it from our latest record table and do not add it to main event
                    try:
                        cursor.execute("DELETE FROM smru_limekiln_latest_detection_events WHERE eventId = %s", (record[0],))
                    except Exception as err:
                        logging.error(f"Error processing record: {err}")

#####################################################################

# Establish connection to the database
@load_env(name="smru_missing_audio_detected_event.connection")                       
def connect_to_database(**kwargs):
    env = kwargs.pop('env')
    conn = psycopg2.connect(
        database=env.db_name,
        user=env.db_user,
        password=env.db_password,
        host=env.db_host,
        port=env.db_port
    )
    conn.autocommit = True
    return conn

#####################################################################

#get the events from the last n-days and return them

def get_detected_events_in_the_last_n_hours(n, api_url):
    # Calculate start and end timestamps
    current_time_milliseconds = int(time.time() * 1000)
    start_timestamp_milliseconds = current_time_milliseconds - (n * 3600 * 1000)  # n hours in milliseconds
    end_timestamp_milliseconds = current_time_milliseconds
    try:
        response = requests.get(api_url.format(start_timestamp_milliseconds, end_timestamp_milliseconds), timeout=20)
        event_data = response.json()
        return event_data
    except Exception as e:
        logging.error(f"Error fetching events: {e}")
    return None

#######################################################################

@load_env("smru_missing_audio_detected_event.api_url_event")
def main(**kwargs):

    #Get the events in the last 4 days and store in event_data
    env = kwargs.pop('env')
    url=env.api_url_event
    event_data = get_detected_events_in_the_last_n_hours(96, url)
    
    conn = connect_to_database()
    cursor = conn.cursor()

    try:
        # Fetch events with missing recording IDs
        cursor.execute("SELECT idstring, recordingidstring, starttime FROM smru_limekiln_latest_detection_events WHERE recordingidstring='None';")
        records = cursor.fetchall()
        process_records(records, cursor, event_data)
    except Exception as e:
        logging.error(f"An error occurred: {e}")
    finally:
        cursor.close()
        conn.close()
if __name__ == "__main__":
    with load_latest("smru_missing_audio_detected_event.log_file") as ctx:
        log_file = ctx.get("log_file")

    # Logging setup
    logging.basicConfig(filename=log_file, level=logging.ERROR)

    main()
