import requests
import yaml
import os.path
import time
import datetime
import psycopg2
import logging
import pytz
from pipeline_env import load_env, load_latest
seconds_2_milliseconds = lambda x : int(round( x.timestamp() * 1000 ))

# Connect to the database
@load_env(name="smru_detections.connection")
def connect_to_database(**kwargs):
    env = kwargs.pop('env')
    return psycopg2.connect(
        database=env.db_name,
        user=env.db_user,
        password=env.db_password,
        host=env.db_host,
        port=env.db_port
    )

##################################
# time zone as UTC
desired_timezone = pytz.utc
# Record the current time
##################################### Handling start and end time for the query
@load_env(name="smru_detections")
def get_start_time(start_time_file, **kwargs):
    env = kwargs.pop('env')
    NOW = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
    if os.path.exists(start_time_file):
        with open(start_time_file, 'r') as file:
            start_time_data = yaml.safe_load(file)
            last_start_time = datetime.datetime.strptime(start_time_data['start_time'], '%Y-%m-%dT%H:%M:%S%z').replace(tzinfo=desired_timezone) 
            start_time = last_start_time - datetime.timedelta(**env.CHECK_WITHIN)
            update_start_time(start_time_file, NOW)
            return start_time
    else:
        # File doesn't exist, create it and set the start time to 6 hours ago in UTC
        last_start_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
        last_start_time = last_start_time - datetime.timedelta(hours=6)
        update_start_time(start_time_file, last_start_time)
        return last_start_time

def update_start_time(start_time_file, current_time):
    start_time_data = {'start_time': current_time.strftime('%Y-%m-%dT%H:%M:%S%z')}
    with open(start_time_file, 'w') as file:
        yaml.dump(start_time_data, file,
                  default_flow_style = False, 
                  allow_unicode = True, 
                  sort_keys=False,
                  encoding = None)

def get_start_and_end_times(start_time_file):
    # Fetch the stored start time 
    stored_start_time = get_start_time(start_time_file)
    stored_end_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
    
    return stored_start_time,stored_end_time


@load_env(name="smru_detections.file_path")
def get_start_end_time(file_path, **kwargs):
    env = kwargs.pop("env")
    # Read the latest time and date that API fetch happened
    file_path = file_path or env.file_path
    start_time, end_time =  get_start_and_end_times(file_path)
    start_timestamp_milliseconds = seconds_2_milliseconds(start_time)
    end_timestamp_milliseconds = seconds_2_milliseconds(end_time)
    return start_timestamp_milliseconds, end_timestamp_milliseconds

@load_env(name="smru_detections.api_url")
@load_env(name="smru_detections.connection")
@load_env(name="smru_detections.file_path")
def main(**kwargs):
    env = kwargs.pop("env")
    api_url =  env.api_url

    start_timestamp_milliseconds, end_timestamp_milliseconds = get_start_end_time(env.file_path)
    
    # Create the API URL
    api_url = api_url.format(start_timestamp_milliseconds, end_timestamp_milliseconds)

    ## Mapping Json keys returned by API endpoint to coluns in the database

    column_mapping = {

      "id": "idstring",
      "deploymentId": "deploymentidstring",
      "recordingId": "recordingidstring",
      "startTime": "starttime",
      "endTime": "endtime",
      "detectionCount": "detectioncount",
      "densestMinuteStartTime": "densestMinuteStartTime",
      "recordingRequested": "recordingRequested",
      "recordingReceived": "recordingReceived",
      "detectionType": "alerttype",
      "batchId": "batchId",
      "hasAnnotation": "hasAnnotation"
    }
    
    # API call
    try:
        response = requests.get(api_url, timeout=20)
        detections = response.json()
        conn = connect_to_database()
        cursor = conn.cursor()
        
        for detection in detections:
            
            mapped_columns = [column_mapping[key] for key in column_mapping.keys()]
            columns = ', '.join(mapped_columns)
            values = ', '.join("'" + str(detection[column]) + "'" for column in column_mapping.keys())

            ##Insert the events details into a table that holds the latest events 
            sql_latest_events = "INSERT INTO %s ( %s ) VALUES ( %s );" % ('smru_limekiln_latest_detection_events', columns, values)
                
            try:
                cursor.execute(sql_latest_events)       
            except psycopg2.Error as err:
                logging.error("Error in executing SQL statement: %s", str(err))
                conn.rollback()

            ##Insert latest events details in to a table that holds events that their annotations are not ready yet

            sql_events_with_no_annotation = "INSERT INTO smru_pending_annotations ( alert_type, start_time, end_time, event_id, recording_id, densest_minute_start_time, has_annotation) VALUES ( '{}','{}', '{}', '{}', '{}', '{}', {})".format(
            detection['detectionType'],detection['startTime'], detection['endTime'], 
            detection['id'], detection['recordingId'], detection['densestMinuteStartTime'], 
            detection['hasAnnotation'])

            try:
                print("We reached here ----> "+ sql_events_with_no_annotation)
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

if __name__ == "__main__":
    with load_latest("smru_detections.log_file") as ctx:
        log_file = ctx.get("log_file")
        # print(ctx)
    # Logging setup
    logging.basicConfig(filename=log_file, level=logging.ERROR)


    main()