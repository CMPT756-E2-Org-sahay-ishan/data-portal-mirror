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


# Configure logging
logging.basicConfig(filename='sample_file.log', level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Database connection details
db_host = 'localhost'
db_user = ''
db_password = 'USE_YOUR_OWN_PASSWORD'
db_name = 'hallo'

# API endpoints
event_api_url = 'api_url'
recording_api_url = 'api_url'

# Directory to save audio recordings
audio_directory = '/data01/audio/smru/random_audio/'


######################################################## Record the current time that the script is running and pass it as a parameter
file_path = "sample_file.yaml"

def get_start_end_time(file_path):
    # TODO: utc.now() and other time functions are deprecated. Update to a modern python (3.12) standard
    NOW = datetime.utcnow().replace(tzinfo=timezone.utc) #tzinfo=pytz.timezone('America/Vancouver')
    start_time = NOW - timedelta(minutes=10) # 10 minutes ago in milliseconds
    end_time = NOW

    # Read the start time from the file
    if os.path.isfile(file_path):
        with open(file_path, "r") as yaml_file:
            get_from_file = yaml.safe_load(yaml_file)
            
            last_download = get_from_file.get('last_download')
            if last_download:
                start_time = last_download['end_time']
                if isinstance(start_time, str):    
                    # start_time = datetime.fromisoformat(start_time_string['end_time']) # python 3.7+
                    start_time = isoparser.parse(start_time)
                elif isinstance(start_time, datetime): # Datetime object
                    pass
                else:
                    logging.error("The value for last_download.start_time is not saved as a string in iso-format. Maybe it needs surrounding quotation (') symbols.")
                    pass

   
    # record times in readable isoformat
    save_to_file = {
        'last_download':{
            'start_time': start_time.isoformat(),
            'end_time': end_time.isoformat(),
            'last_run': NOW.isoformat()
        }
    }
    logging.info(f"{save_to_file}")

    # Write the end time to the file
    with open(file_path, "w") as yaml_file:
        yaml.dump(save_to_file, yaml_file,
                  default_flow_style = False, 
                  allow_unicode = True, 
                  sort_keys=False,
                  encoding = None)

    return (int(round( _.timestamp() * 1000 )) for _ in [start_time, end_time]) # convert seconds to milliseconds
#########################################################
## Note ----->
## For the first time that the script is executed, the start time is from 600 minutes ago
## For the next time, the start time is from the latest time that the script was executed and end time the current time.        
########################################################


# Function to fetch events from the API
def fetch_events(start_time, end_time):
    """Fetch events from API Endpoint

    Args:
        start_time (int): epoch start time in milliseconds
        end_time (int): epoch end_time in milliseconds

    Returns:
        dict | None: JSON response of http request to API
    """
    try:
        response = requests.get(event_api_url.format(start_time, end_time))
        response.raise_for_status()  # Raise an HTTPError for bad responses
        return response.json()
    except requests.exceptions.RequestException as e:
        logging.error(f"Error fetching events: {e}")
        return None

# Function to download audio recording
def download_recording(recording_id):
    print(f'download_recording')

    recording_path = os.path.join(audio_directory, f'{recording_id}.wav')
    
    if os.path.isfile(recording_path):
        logging.info(f"Recording {recording_id}.wav is already downloaded.")
        return recording_path
    print(f'donwloading {recording_id} to {recording_path}')
    try:
        response = requests.get(recording_api_url.format(recording_id))
        response.raise_for_status()
        print('download success')
        with open(recording_path, 'wb') as audio_file:
            audio_file.write(response.content)
        print('download and write success')
        return recording_path
    except requests.exceptions.RequestException as e:
        print('download fail')
        logging.error(f"Error downloading recording {recording_id}: {e}")
        return None
    

# Function to insert event metadata into the PostgreSQL database
def insert_into_database(event):
    try:
        connection = psycopg2.connect(host=db_host, user=db_user, password=db_password, dbname=db_name)
        with connection.cursor() as cursor:
            # SQL query
            sql = "INSERT INTO smru_sample_events (alert_type, start_time, end_time, event_id, deployment_id, recording_id) VALUES (%s, %s, %s, %s, %s, %s)"
            cursor.execute(sql, (event['alertType'], event['startTime'], event['endTime'], event['idString'],
                                 event['deploymentIdString'], event['recordingIdString']))
        connection.commit()
    except psycopg2.Error as e:
        logging.error(f"Error inserting into database: {e}")
    finally:
        if connection:
            connection.close()

# Main script
def main():
    # Fetch events from the last time that the script was executed
    
   
    start_time, end_time =get_start_end_time(file_path)

    print(f"start_time={start_time}, end_time={end_time}, num_events={len(events)}")

    if events:
        for event in events:
            # Download audio recording
            recording_id = event.get('recordingIdString')
            print(f"event={event}, recording_id={recording_id}")
            if recording_id:
                recording_path = download_recording(recording_id)
                if recording_path:
                    logging.info(f"Recording {recording_id} downloaded successfully to {recording_path}")
                else:
                    logging.warning(f"Failed to download recording {recording_id}")

            # Insert event metadata into the PostgreSQL database
            insert_into_database(event)
            logging.info(f"Completed event {event['idString']}")
    print(f"start_time={start_time}, end_time={end_time}, num_events={len(events)} done", end='\n\n')
    
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='')
    parser.add_argument('-m', '--message', type=str, help='Any message to be sent to the logger.')
    args = parser.parse_args()
    if args.message:
        logging.info(args.message)

    main()