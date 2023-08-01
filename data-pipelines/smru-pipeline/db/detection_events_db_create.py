##########Insert data into the database
import psycopg2

###Connect to the database
conn = psycopg2.connect(
   database="",
    user='',
    password='USE_YOUR_OWN_PASSWORD',
    host='localhost',
    port= '0')
conn.autocommit = True
cursor = conn.cursor()
sql = '''CREATE TABLE detection_events(idString text primary key,
          alertType text,
          startTime text, 
          endTime text,
          deploymentIdString text, 
          recordingIdString text,
          detectionCount int, 
          densestMinuteStartTime text,
          recordingRequested boolean, 
          recordingReceived boolean, 
          detectionType text,
          batchId bigint, 
          hasAnnotation boolean 
          );'''

cursor.execute(sql)
conn.close()
print("database table created successfully")
