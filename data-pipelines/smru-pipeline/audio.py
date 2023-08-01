import requests
import json
import os.path
import time
import datetime
import psycopg2
import urllib
import shutil
from urllib.request import urlopen
from shutil import copyfileobj

#create the api url
api_url = "http://smruna.eastus.cloudapp.azure.com:9080/recording/wav?recordingId="

################################ Logging Errors

def log_error(logfile, error_general_message, error_detailed_message):
    f = open(logfile, "a")
    f.write(error_general_message +str(datetime.datetime.now()) + "--->" +  error_detailed_message + "\n")
    f.close()
#################################

######################### Flatten Json returned by (SMRU) APIs to a dictionary
def flatten_json(y):
    out = {}
 
    def flatten(x, name=''):
 
        # If the Nested key-value
        # pair is of dict type
        if type(x) is dict:
 
            for a in x:
                flatten(x[a], name + a + '_')
 
        # If the Nested key-value
        # pair is of list type
        elif type(x) is list:
 
            i = 0
 
            for a in x:
                flatten(a, name + str(i) + '_')
                i += 1
        else:
            out[name[:-1]] = x
 
    flatten(y)
    return out
###########################################

#####Path to location that the audio file will be stored
audio_path="/home/centos/smru_audio/"
#Connect to the database

conn = psycopg2.connect(
   database="",
    user='',
    password='USE_YOUR_OWN_PASSWORD',
    host='localhost',
    port= '0')
conn.autocommit = True
cursor = conn.cursor()
try:
    sql = """SELECT * from smru_new_detection_events"""
    cursor.execute(sql)
    records = cursor.fetchall()
    #print(records)
     ############### download the audio files
    for record in records:
        
       
        url=api_url+record[5]
        try:
            with urllib.request.urlopen(url) as response, open(audio_path+record[0]+'.wav', 'wb') as out_file:
                shutil.copyfileobj(response, out_file)
        except urllib.error.HTTPError as e:
            print (e)

       
    ################# Fetch details
    ant_url='api_url'
    for record in records:
        url=ant_url+record[0]
        #print(url)
        try:
            response = requests.get(url,timeout=20)
            detection_events=response.json()
            null="None"
            for detection_details in detection_events:
                flatten_detection=flatten_json(detection_details)
                #print(flatten_detection) 
                values = ', '.join("'" + str(x)+ "'"  for x in flatten_detection.values())
                sql = "INSERT INTO %s  VALUES ( %s );" % ('smru_detection_details', values)
                #print(sql)
                try:
                    cursor.execute(sql)
                except Exception as err:
                    log_error("error_log.txt","Error in inserting data in the database smru_detection_details: ", str(err))

        
        except requests.exceptions.HTTPError as errh:
            log_error("error_log.txt","Error in fetching smru detections details:  ", str(errh))
        except requests.exceptions.ConnectionError as errc:
            log_error("error_log.txt","Error in fetching smru detections details:  ", str(errc))
            print ("Error Connecting:",errc)
        except requests.exceptions.Timeout as errt:
            log_error("error_log.txt","Error in fetching smru detections details:  ", str(errt))
            print ("Timeout Error:",errt)
        except requests.exceptions.RequestException as err:
            log_error("error_log.txt","Error in fetching smru detections details:  ", str(err))
            print ("OOps: Something Else",err)
            
    ########################################### Fetch Annotations
    # TO DO
    ############################################
        
except Exception as err:
    print("Failed to read data from table", str(err))
    log_error("error_log.txt", "Error in reading data from database table smru detection events:  ", str(err))
                                                                                                                                           
######################### truncate table new_detection_events 
sqlTruncateTable='TRUNCATE TABLE new_detection_events;'
try:
    cursor.execute(sqlTruncateTable)
except Exception as err:
    log_error("error_log.txt","Error in truncating table smru_new_detections_events:  ", str(err))

                                                                                                                                           
                                                                                                                                          
                                                                                                                                           
                                                                                                                                           
                                                                                                                                           