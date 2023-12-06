import requests
import logging
from datetime import datetime

# logging configuration
log_filename = "log.txt"
logging.basicConfig(filename=log_filename, level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

#base URL
base_url = "https://portal.orca.research.sfu.ca/api"

# Authenticate and create JWT token
def authenticate(username, password):
    endpoint_url = f"{base_url}/auth/login"
    auth_data = {"username": username, "password": password}
    response = requests.post(endpoint_url, json=auth_data)
    
    if response.status_code == 200:
        logging.info("Authentication successful")
        return response.headers.get("Authorization")
    else:
        logging.error(f"Authentication failed: {response.status_code} - {response.text}")
        return None



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


# download audio for a specific event (sample or detected)
def download_audio(event_id,jwt):
    endpoint_url = f"{base_url}/smru/sampleEvent/audio/{event_id}"
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

# post annotation for a specific event
def post_annotation(event_id, confidence, isOrcaFound, jwt):
    headers = {"Authorization": f"Bearer {jwt}"}
    endpoint_url = f"{base_url}/annotations/smru/ml"
    annotation_data = {
        "eventId": event_id,
        "confidence": confidence,    ###If there is a percentage for the decision is recorded here
        "isOrcaFound":isOrcaFound   #### The value is True or False
    }
    response = requests.post(endpoint_url, json=annotation_data, headers=headers)
    
    if response.status_code == 201:
        logging.info(f"Annotation posted for event {event_id}")
    else:
        logging.error(f"Error posting annotation for event {event_id}: {response.status_code} - {response.text}")

# start and end times
start_time = "2023-11-01 00:00:00"
end_time = "2023-11-12 23:59:59"

###### Note: This can be automated based on the current time and the time of the last fetch recorded on a local file



# Specify your username and password for authentication
username = "your username"
password = "your password"

# Authenticate and obtain JWT token
jwt_token = authenticate(username, password)

if jwt_token:
    # Fetch events between the specified start and end times
    events = fetch_events_between_times_with_token(start_time, end_time, jwt_token)

    if events:
        for event in events:
            event_id = event["event_id"]
            
            # Download audio for the event
            audio_filename = download_audio_with_token(event_id, jwt_token)
            
            #####################################################

            ###### HERE, MACHINE LEARNING MODEL IS CALLED AND IT DECIDES IF THE FILE IS A TRUE DETECTION
          
            #####################################################

            if audio_filename:
                # Post ML decision for the event. 
                post_annotation(event_id, confidence=0.9, isOrcaFound=True, jwt_token)  


# ---> NOTE
"""
Confidence Value and isOrceFound are hard-coded 
but they need to be set based on the result of
the ML model's decision
"""
###########################################
