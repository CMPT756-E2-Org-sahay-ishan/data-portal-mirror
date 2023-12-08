import requests
import psycopg2
from datetime import datetime, timedelta
import os
import logging
import time

# Configure logging
logging.basicConfig(filename='sample_file.log', level=logging.ERROR, format='%(asctime)s - %(levelname)s - %(message)s')

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
file_path = "sample_file.txt"

def get_start_end_time(file_path):
    # Read the start time from the file
    if os.path.isfile(file_path):
        with open(file_path, "r") as file:
            start_time_string = file.read().strip()
            start_time = int(start_time_string) if start_time_string else int(round(datetime.utcnow().timestamp() * 1000)) - 600000
    else:
        start_time = int(round(datetime.utcnow().timestamp() * 1000)) - 600000

    # Record the current time in milliseconds as the end time
    end_time = int(round(datetime.utcnow().timestamp() * 1000))

    # Write the end time to the file
    with open(file_path, "w") as file:
        file.write(str(end_time))

    return start_time, end_time
#########################################################
## Note ----->
## For the first time that the script is executed, the start time is from 600 minutes ago
## For the next time, the start time is from the latest time that the script was executed and end time the current time.        
########################################################


# Function to fetch events from the API
def fetch_events(start_time, end_time):
    try:
        response = requests.get(event_api_url.format(start_time, end_time))
        response.raise_for_status()  # Raise an HTTPError for bad responses
        return response.json()
    except requests.exceptions.RequestException as e:
        logging.error(f"Error fetching events: {e}")
        return None

# Function to download audio recording
def download_recording(recording_id):
    try:
        response = requests.get(recording_api_url.format(recording_id))
        response.raise_for_status()
        recording_path = os.path.join(audio_directory, f'{recording_id}.wav')
        with open(recording_path, 'wb') as audio_file:
            audio_file.write(response.content)
        return recording_path
    except requests.exceptions.RequestException as e:
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

    events = fetch_events(start_time, end_time)

    if events:
        for event in events:
            # Download audio recording
            recording_id = event.get('recordingIdString')
            if recording_id:
                recording_path = download_recording(recording_id)
                if recording_path:
                    logging.info(f"Recording {recording_id} downloaded successfully to {recording_path}")
                else:
                    logging.warning(f"Failed to download recording {recording_id}")

            # Insert event metadata into the PostgreSQL database
            insert_into_database(event)

if __name__ == "__main__":
    main()