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
from psycopg2 import sql

# Configure logging
logging.basicConfig(filename='sample_file.log', level=logging.ERROR)

# Create the API URL
api_url = "http://smruna.eastus.cloudapp.azure.com:9080/recording/wav?recordingId="
audio_path = "DETECTED_FOLDER/"



# Function to download audio file
def download_audio(url, file_path):
    print(url, file_path)
    try:
        with urllib.request.urlopen(url) as response, open(file_path, 'wb') as out_file:
            shutil.copyfileobj(response, out_file)
    except urllib.error.HTTPError as e:
        logging.error(f"HTTP Error: {e}")

# process_records function
def process_records(records, cursor):
    for record in records:
        recording_id = None
        if record[1]!="None":  # Check if recordingId exists
            recording_id = record[1]

        if recording_id:  # Check if recordingId exists and is not null
            url = api_url + recording_id
            file_path = os.path.join(audio_path, record[0] + '.wav')
            download_audio(url, file_path)  # Download audio
            try:

                sql_query = sql.SQL("""
                    INSERT INTO smru_limekiln (
                        event_type,            
                        event_id,
                        alert_type,
                        start_time,
                        end_time,
                        deployment_id,
                        recording_id,
                        detection_count,
                        densest_minute_start_time,
                        recording_requested,
                        recording_received,
                        detection_type,
                        batch_id,
                        has_annotation
                    )
                    SELECT 
                        'DETECTED' AS event_type,
                        idstring AS event_id,
                        alerttype AS alert_type,
                        TO_TIMESTAMP(starttime, 'YYYY-MM-DD"T"HH24:MI:SS"Z"')::TIMESTAMP WITHOUT TIME ZONE AS start_time,
                        TO_TIMESTAMP(endtime, 'YYYY-MM-DD"T"HH24:MI:SS"Z"')::TIMESTAMP WITHOUT TIME ZONE AS end_time,
                        deploymentidstring AS deployment_id,
                        recordingidstring AS recording_id,
                        detectioncount AS detection_count,
                        densestminutestarttime AS densest_minute_start_time,
                        recordingrequested AS recording_requested,
                        recordingreceived AS recording_received,
                        detectiontype AS detection_type,
                        batchid AS batch_id,
                        hasannotation AS has_annotation
                    FROM 
                        smru_limekiln_latest_detection_events
                    WHERE 
                        idstring = %s
                """)
                cursor.execute(sql_query, (record[0],)) #Insert into main event table
                cursor.execute("DELETE FROM smru_limekiln_latest_detection_events WHERE idstring = %s", (record[0],)) #remove event from temporary table
            except Exception as err:
                 logging.error(f"Error processing record: {err}")

def main():
    conn = psycopg2.connect(
        database="",
        user='',
        password='USE_YOUR_OWN_PASSWORD',
        host='localhost',
        port='0'
    )
    conn.autocommit = True
    cursor = conn.cursor()
    try:
        sql = """SELECT idstring,recordingidstring FROM smru_limekiln_latest_detection_events"""
        cursor.execute(sql)
        records = cursor.fetchall()
        process_records(records, cursor)
    except Exception as err:
        logging.error(f"Failed to read data from table: {err}")
    finally:
        if cursor is not None:
            cursor.close()
        if conn is not None:
            conn.close()
            
if __name__ == "__main__":
    main()


