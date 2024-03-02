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
from pipeline_env import load_env, load_latest

# Configure logging

# Create the API URL

# Function to download audio file
def download_audio(url, file_path):
    print(url, file_path)
    try:
        with urllib.request.urlopen(url) as response, open(file_path, 'wb') as out_file:
            shutil.copyfileobj(response, out_file)
    except urllib.error.HTTPError as e:
        logging.error(f"HTTP Error: {e}")

# process_records function
@load_env("download_detected_audio.api_url")
@load_env("download_detected_audio.audio_path")
def process_records(records, cursor, **kwargs):
    env = kwargs.get("env")

    for record in records:
        recording_id = None
        if record[1]!="None":  # Check if recordingId exists
            recording_id = record[1]

        if recording_id:  # Check if recordingId exists and is not null
            url = env.api_url + recording_id
            file_path = os.path.join(env.audio_path, record[0] + '.wav')
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

@load_env("download_detected_audio.connection")
def main(**kwargs):
    env = kwargs.get("env")

    conn = psycopg2.connect(
        database=env.db_name,
        user=env.db_user,
        password=env.db_password,
        host=env.db_host,
        port=env.db_port
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

    with load_latest("download_detected_audio.log_file") as ctx:
        log_file = ctx.get("log_file")

    # Logging setup
    logging.basicConfig(filename=log_file, level=logging.ERROR)

    main()


