import requests
import json
import os.path
import time
import datetime
import psycopg2

################################ Logging Errors
def log_error(logfile, error_general_message, error_detailed_message):
    f = open(logfile, "a")
    f.write(error_general_message +str(datetime.datetime.now()) + "--->" +  error_detailed_message + "\n")
    f.close()
#################################

#Connect to the database

conn = psycopg2.connect(
   database="",
    user='',
    password='USE_YOUR_OWN_PASSWORD',
    host='localhost',
    port= '0')
conn.autocommit = True
cursor = conn.cursor()

#Record the current time

current_time_millis = int(round(time.time() * 1000))

#Read the latest time and date that api fetch happend
path="./sample_file.txt"
if os.path.isfile(path):
    f=open("sample_file.txt", "r")
    latest_call_time=f.read()
    f.close()
    latest_call_time=int(latest_call_time)
else:
    latest_call_time=current_time_millis-600000

#create the api url
api_url = "api_url".format(latest_call_time,current_time_millis)

#api call

try:
    response = requests.get(api_url,timeout=20)
    detections=response.json()
    for detection in detections:
        columns = ', '.join("" + str(x) + "" for x in detection.keys())
        values = ', '.join("'" + str(x) + "'"  for x in detection.values())
        sql1 = "INSERT INTO %s ( %s ) VALUES ( %s );" % ('smru_detection_events', columns, values)
        sql2 = "INSERT INTO %s ( %s ) VALUES ( %s );" % ('smru_new_detection_events', columns, values)
        try:
            cursor.execute(sql1)
            cursor.execute(sql2)
        except Exception as err:
            log_error("error_log.txt","Error in inserting data in the database smru_detections_events:  ", str(err))

##Error handling for api call
except requests.exceptions.HTTPError as errh:
    log_error("error_log.txt","Error in fetching smru detections events:  ", str(errh))
except requests.exceptions.ConnectionError as errc:
    log_error("error_log.txt","Error in fetching smru detections events:  ", str(errc))
    print ("Error Connecting:",errc)
except requests.exceptions.Timeout as errt:
    log_error("error_log.txt","Error in fetching smru detections events:  ", str(errt))
    print ("Timeout Error:",errt)
except requests.exceptions.RequestException as err:
    log_error("error_log.txt","Error in fetching smru detections events:  ", str(err))
    print ("OOps: Something Else",err)
