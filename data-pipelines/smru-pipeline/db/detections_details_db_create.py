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
sql = '''CREATE TABLE detection_details(
          
          timestamp_detection bigint,
          date_string_detection text,
          idString text primary key,
          date_string_event text,
          date_milisecond_event bigint, 
          recordingId text, 
          contour_ridge text, 
          channel_map int, 
          start_sample int, 
          n_slice int, 
          amplitude real, 
          frequency_bounds_min real, 
          frequency_bounds_max real, 
          bearing_Re_Array real, 
          bearing_Re_N real,
          bearing_Error real, 
          duration_Samples bigint, 
          duration_Milliseconds bigint
          );'''

cursor.execute(sql)
conn.close()
print("database table created successfully")
