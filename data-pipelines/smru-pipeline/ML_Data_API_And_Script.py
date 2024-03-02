import requests
import logging
import os
import json
import datetime
import pytz

# time zone as UTC
desired_timezone = pytz.utc

# logging configuration
log_filename = "log.txt"

logging.basicConfig(filename=log_filename, level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

#base URL
base_url = "https://portal.orca.research.sfu.ca/api"

##################################### Handling start and end time for the query
def get_start_time():
    start_time_file = './start_time.json'
    if os.path.exists(start_time_file):
        with open(start_time_file, 'r') as file:
            start_time_data = json.load(file)
            return datetime.datetime.fromisoformat(start_time_data['start_time']).replace(tzinfo=desired_timezone)
    else:
        # File doesn't exist, create it and set the start time to  6 hours ago in UTC
        current_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone)
        current_time = current_time - datetime.timedelta(hours=6)
        update_start_time(current_time)
        return current_time

def update_start_time(current_time):
    start_time_file = './start_time.json'
    start_time_data = {'start_time': current_time.isoformat()}
    with open(start_time_file, 'w') as file:
        json.dump(start_time_data, file)

def get_start_and_end_times():
    # Fetch the stored start time 
    stored_start_time = get_start_time()
    start_time = stored_start_time.strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]  # Convert to string
    end_time = datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone).strftime('%Y-%m-%d %H:%M:%S.%f')[:-3]  # Convert to string
  # Update the JSON file with the current time
    update_start_time(datetime.datetime.utcnow().replace(microsecond=0, tzinfo=desired_timezone))

    return start_time, end_time

##################################### Authentication for accessing API endpoints


# Authenticate and create JWT token
def authenticate(username, password):
    endpoint_url = f"{base_url}/auth/login"
    print("we are authenticating")
    auth_data = {"username": username, "password": password}
    response = requests.post(endpoint_url, json=auth_data)
    print(response)
    if response.status_code == 200:
        logging.info("Authentication successful")
        print(response.headers)
        return response.headers.get("Authorization")
    else:
        logging.error(f"Authentication failed: {response.status_code} - {response.text}")
        return None
    
########################################### Fetch sample events

def fetch_events_between_times(start_time, end_time,jwt):
    endpoint_url = f"{base_url}/smru/sampleEvent"
    headers = {"Authorization": f"Bearer {jwt}"}
    response = requests.get(endpoint_url, params={"startTime1": start_time, "startTime2": end_time}, headers=headers)
    
    if response.status_code == 200:
        return response.json()
    else:
        logging.error(f"Error fetching events: {response.status_code} - {response.text}")
        return None
    
############################################## Download sample events audio files
def download_audio(event_id,jwt):
    endpoint_url = f"{base_url}/smru/sampleEvent/audio/{event_id}"
    print(endpoint_url)
    headers = {"Authorization": f"Bearer {jwt}"}
    response = requests.get(endpoint_url, headers=headers)
    
    if response.status_code == 200:
      
        filename = f"audio_{event_id}.wav"
        with open(filename, "wb") as audio_file:
            audio_file.write(response.content)
        logging.info(f"Audio downloaded for event {event_id}")
        return filename
    else:
        logging.error(f"Error downloading audio for event {event_id}: {response.status_code} - {response.text}")
        return None
    
#################################################### Post ML model's label for each sample event
# post annotation for a specific event
def post_annotation(event_id, confidence, isOrcaFound, jwt):
    headers = {"Authorization": f"Bearer {jwt}"}
    endpoint_url = f"{base_url}/smru/label/ml"
    annotation_data = {
        "eventId": event_id,
        "confidence": confidence,    ###If there is a percentage for the decision is recorded here
        "ifItIsOrca":isOrcaFound   #### The value is True or False
    }
    response = requests.post(endpoint_url, json=annotation_data, headers=headers)
    print(response.status_code)
    if response.status_code ==200 :
        logging.info(f"Annotation posted for event {event_id}")
    else:
        logging.error(f"Error posting annotation for event {event_id}: {response.status_code} - {response.text}")

###################################################### Fetch sample events between start and end time
# fetch sample events
def fetch_events_between_times(start_time, end_time,jwt):
    endpoint_url = f"{base_url}/smru/sampleEvent"
    headers = {"Authorization": f"Bearer {jwt}"}
    response = requests.get(endpoint_url, params={"startTime1": start_time, "startTime2": end_time}, headers=headers)
    
    if response.status_code == 200:
        return response.json()
    else:
        logging.error(f"Error fetching events: {response.status_code} - {response.text}")
        return None
    
###################################################### Main part

# Specify your username and password for authentication

username = "your username"
password = "your password"

# Authenticate and obtain JWT token
jwt_token = authenticate(username, password)

if jwt_token:
    # Fetch events between the specified start and end times
    events = fetch_events_between_times(start_time1, start_time2, jwt_token)

    if events:
        for event in events:
            event_id = event["eventId"]
            
            # Download audio for the event
            audio_filename = download_audio(event_id, jwt_token)

            if audio_filename:
                           
#####################################################

                ## HERE, MACHINE LEARNING MODEL IS CALLED AND IT DECIDES IF THE AUDIO IS A TRUE DETECTION
                ##As an example, result is set True
                result=True
                # Post annotation for the event
                post_annotation(event_id, 1, result, jwt_token)
########################################################

# ---> NOTE
"""
Confidence Value and isOrceFound are hard-coded 
but they need to be set based on the result of
the ML model's decision
"""
###########################################