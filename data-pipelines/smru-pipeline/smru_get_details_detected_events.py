##WIP##

##This is a script to fetch details of each event which includes in milliseconds, all detections that happened within a 20 minutes detected event


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

# Flatten JSON returned by SMRU APIs to a dictionary
def flatten_json(y):
    out = {}
    def flatten(x, name=''):
        if type(x) is dict:
            for a in x:
                flatten(x[a], name + a + '_')
        elif type(x) is list:
            i = 0
            for a in x:
                flatten(a, name + str(i) + '_')
                i += 1
        else:
            out[name[:-1]] = x
    flatten(y)
    return out


@load_env(name='smru_get_details_detected_events.ant_url')
def fetch_details(record, **kwargs):
    env = kwargs.get('env')

    recording_id = None
    url = env.ant_url + record[0]  # Assuming record[0] contains eventId
    try:
        response = requests.get(url, timeout=20)
        detection_events = response.json()
        
        for detection_details in detection_events:
            flatten_detection = flatten_json(detection_details)
            print(flatten_detection)
            if 'recordingId' in flatten_detection:
                recording_id = flatten_detection['recordingId']
                if recording_id=="None":
                    recording_id=None
                ##TODO. Store detection details in a separate table. For now, we don't do it. Anytime it is required, we can separately read from smru_limekiln and for each event_id, fetch event details

    except requests.exceptions.RequestException as err:
        logging.error(f"Error in fetching smru detections details: {err}")
    return recording_id


def main():
    # TODO: to be implemented
    pass


if __name__ == "__main__":
    
    with load_latest("smru_get_details_detected_events.log_file") as ctx:
        log_file = ctx.get("log_file")

    # Logging setup
    logging.basicConfig(filename=log_file, level=logging.ERROR)

    main()
