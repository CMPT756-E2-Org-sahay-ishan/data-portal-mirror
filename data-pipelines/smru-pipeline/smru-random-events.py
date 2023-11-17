import requests
import psycopg2
from datetime import datetime, timedelta
import os
import logging

# Configure logging
logging.basicConfig(filename='smru_data_pipeline.log', level=logging.ERROR, format='%(asctime)s - %(levelname)s - %(message)s')

# Database connection details
db_host = 'localhost'
db_user = ''
db_password = 'USE_YOUR_OWN_PASSWORD'
db_name = 'hallo'

# API endpoints
event_api_url = 'api_url'
recording_api_url = 'api_url'

# Directory to save audio recordings
audio_directory = '/data01/audio/smru'
#audio_directory = './smru'

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
    # Fetch events from the last 1000 minutes
    end_time = datetime.utcnow()
    start_time = end_time - timedelta(minutes=1000)

    events = fetch_events(round(start_time.timestamp() * 1000), round(end_time.timestamp() * 1000))

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