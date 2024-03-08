-- Create database tables
CREATE TABLE detection_events(idString text primary key,
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
          );

CREATE TABLE detection_details(
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
          );