import requests
import psycopg2
from datetime import datetime, timedelta, timezone
from dateutil import parser as isoparser # TODO: From python 3.7 you can use datetime.datetime.fromisoformat
import pytz
import os
import logging
import time
import yaml
import argparse
from pipeline_env import load_env, load_latest

# Configure logger

# Database connection details

# API endpoints

# Directory to save audio recordings

seconds_2_milliseconds = lambda x : int(round( x.timestamp() * 1000 ))
######################################################## Record the current time that the script is running and pass it as a parameter

@load_env(name="smru_random_events")
def get_start_end_time(file_path:str, **kwargs):

    env = kwargs.pop('env')
    file_path = file_path or env.file_path
    
    # TODO: utc.now() and other time functions are deprecated. Update to a modern python (3.12) standard
    NOW = datetime.utcnow().replace(tzinfo=timezone.utc) #tzinfo=pytz.timezone('America/Vancouver')
    start_time = NOW - timedelta(minutes=10) # 10 minutes ago in milliseconds
    end_time = NOW
    last_download_time = None

    # Read the start time from the file
    if os.path.isfile(file_path):
        with open(file_path, "r") as yaml_file:
            get_from_file = yaml.safe_load(yaml_file)
            
            last_fetch = get_from_file.get('last_fetch')
            last_download = last_fetch.get('last_download')
            if last_download:
                # start_time = last_download['end_time']
                last_download_time = last_download.get('recording_endTime')
                if isinstance(last_download_time, str):    
                    # start_time = datetime.fromisoformat(start_time_string['end_time']) # python 3.7+
                    last_download_time = isoparser.parse(last_download_time)
                elif isinstance(last_download, datetime): # Datetime object
                    pass
                else:
                    logging.error("The value for last_fetch.last_download.last_download_time is not saved as a string in iso-format. Maybe it needs surrounding quotation (') symbols.")
                    pass
    
    if last_download_time: # TODO: Condition for DEV
    # if last_download_time and  NOW - timedelta(**env.CHECK_WITHIN) <= last_download_time <= NOW: # TODO: Condition for PROD
        end_time = NOW
        start_time = last_download_time

    else:
        end_time = NOW
        start_time = NOW - timedelta(**env.DOWNLOAD_WITHIN)


    return start_time, end_time, NOW

@load_env(name="smru_random_events.file_path")
def save_to_file(dict_info:dict, file_path:str=None, **kwargs):
    env = kwargs.pop('env')
    

    # Write the end time to the file
    with open(env.file_path, "w") as yaml_file:
        yaml.dump(dict_info, yaml_file,
                  default_flow_style = False, 
                  allow_unicode = True, 
                  sort_keys=False,
                  encoding = None)
#########################################################
## Note ----->
## For the first time that the script is executed, the start time is from 600 minutes ago
## For the next time, the start time is from the latest time that the script was executed and end time the current time.        
########################################################


# Function to fetch events from the API
@load_env(name="smru_random_events.event_api_url")
def fetch_events(start_time:int, end_time:int, **kwargs):
    """Fetch events from API Endpoint

    Args:
        start_time (int): epoch start time in milliseconds
        end_time (int): epoch end_time in milliseconds

    Returns:
        dict | None: JSON response of http request to API
    """
    env = kwargs.pop('env')
    try:
        response = requests.get(env.event_api_url.format(start_time, end_time))
        response.raise_for_status()  # Raise an HTTPError for bad responses
        return response.json()
    except requests.exceptions.RequestException as e:
        logging.error(f"Error fetching events: {e}")
        return None

# Function to download audio recording
@load_env(name="smru_random_events.recording_api_url")
@load_env(name="smru_random_events.audio_directory")
def download_recording(id_string, recording_id, **kwargs):
    env = kwargs.pop('env')

    print(f'download_recording')

    file_path = os.path.join(env.audio_directory, f'{id_string}.wav')
    
    if os.path.isfile(file_path):
        logging.info(f"Recording {id_string}.wav is already downloaded.")
        return file_path
    print(f'downloading {id_string} to {file_path}')
    try:
        response = requests.get(env.recording_api_url.format(recording_id))
        response.raise_for_status()
        if response.content:
            with open(file_path, 'wb') as audio_file:
                audio_file.write(response.content)
            print('download and write success')
        return file_path
    except (requests.exceptions.RequestException, requests.exceptions.Timeout) as e:
        print('download fail')
        logging.error(f"Error downloading recording {id_string}: {e}")
        return None
    

# Function to insert event metadata into the PostgreSQL database
@load_env(name="smru_random_events.connection")
def insert_into_database(event, **kwargs):
    env = kwargs.pop('env')
    
    try:
        connection = psycopg2.connect(host=env.db_host, port=env.db_port, user=env.db_user, password=env.db_password, dbname=env.db_name)
        with connection.cursor() as cursor:
            # SQL query
            sql = "INSERT INTO smru_limekiln (event_type,alert_type, start_time, end_time, event_id, deployment_id, recording_id) VALUES (%s,%s, %s, %s, %s, %s, %s)"
            cursor.execute(sql, ('RANDOM',event['alertType'], event['startTime'], event['endTime'], event['idString'],
                                 event['deploymentIdString'], event['recordingIdString']))
        connection.commit()
    except psycopg2.Error as e:
        logging.error(f"Error inserting into database: {e}")
        # if e.pgcode == psycopg2.errorcodes.UniqueViolation:
        #     logging.info(f"Updating recording_id of event_id={event['idString']}")
        #     Update the existing record in the database with the recording_id
        #     connection = psycopg2.connect(host=db_host, user=db_user, password=db_password, dbname=db_name)
        #     with connection.cursor() as cursor:
        #         sql = f"UPDATE smru_sample_events SET recording_id={event['recordingIdString']} WHERE event_id={event['idString']}"
        #         cursor.execute(sql)
        #     connection.commit()
    finally:
        if connection:
            connection.close()

# Main script
@load_env(name="smru_random_events.file_path")
def main(**kwargs):
    # Fetch events from the last time that the script was executed
    
    env = kwargs.pop('env')
   
    start_time, end_time, NOW = get_start_end_time(env.file_path)

    events = fetch_events(seconds_2_milliseconds(start_time),
                          seconds_2_milliseconds(end_time))
    print(f"start_time={start_time}, end_time={end_time}, num_events={len(events)}")

    # record times in readable isoformat
    dict_info = {
        'last_fetch':{
            'start_time': start_time.isoformat(),
            'end_time': end_time.isoformat(),
            "num_events": len(events),
            'invoke_time': NOW.isoformat()
        }
    }
    
    if events:
        for event in events:
            # Download audio recording
            id_string = event.get('idString')
            recording_id = event.get('recordingIdString')

            print(f"event={event}, id_string={id_string}, recording_id={recording_id}")
            if id_string and recording_id:
                recording_path = download_recording(id_string, recording_id)
                if recording_path:
                    logging.info(f"Recording {id_string} downloaded successfully to {recording_path}")
                    dict_info['last_fetch']['last_download'] = {
                        'id_string': id_string,
                        'recording_endTime' : event['endTime']
                    }
                else:
                    logging.warning(f"Failed to download recording {id_string}")

                # Insert event metadata into the PostgreSQL database
                insert_into_database(event)
            logging.info(f"Completed event {event['idString']}")

    save_to_file(dict_info)
    print(f"start_time={start_time}, end_time={end_time}, num_events={len(events)} done", end='\n\n')
    
if __name__ == "__main__":
    
    with load_latest("smru_random_events.log_file") as ctx:
        log_file = ctx.get("log_file")
        # print(ctx)
    logging.basicConfig(filename=log_file, level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
    
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('-m', '--message', type=str, help='Any message to be sent to the logger.')
    args = parser.parse_args()
    if args.message:
        logging.info(args.message)

    main()