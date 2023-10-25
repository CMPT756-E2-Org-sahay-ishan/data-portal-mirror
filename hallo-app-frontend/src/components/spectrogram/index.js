import React, { useEffect, useRef, useState } from 'react';
import { useLocalState } from '../util/useLocalStorage';
import WaveSurfer from 'wavesurfer.js';
import AudioHeatmap from '../test';



function Spectrogram({ apiUrl }){
    let url='api/smru/audio/'+'64b72a600687e9006d27899c';
    const[jwt, setJwt]=useLocalState("", "jwt");
    const waveformRef = useRef(null);
    const canvasRef = useRef(null);

    const audioDataArrayBuffer = new Float32Array([0.2, 0.5, 0.8, 0.3, 0.9, 0.6, 0.4, 0.7, 0.1]).buffer;

   
  ////////
  const [audioData, setAudioData] = useState(null);
  const [audioObjectURL, setAudioObjectURL] = useState(null);
  const audioPlayerRef = React.createRef();

  const [spectrogramData, setSpectrogramData] = useState(null);


  const fetchAudio = async () => {
    try {
      const response = await fetch(apiUrl, {
        headers:{
          "Content-Type":"application/json",
          Authorization:`Bearer ${jwt}`
        }, 
        method:"GET"
      });
      const data = await response.arrayBuffer();
      console.log("This is array buffer of audio " + data)
      setAudioData(data);
      audioPlayerRef.current.src = URL.createObjectURL(new Blob([data]));

    } catch (error) {
      console.error('Error fetching audio:', error);
    }
  };

  useEffect(() => {
    fetchAudio(); // Fetch audio when the component mounts

    return () => {
      // Revoke the object URL when the component unmounts
      if (audioObjectURL) {
        URL.revokeObjectURL(audioObjectURL);
      }
    };
  }, [apiUrl, audioObjectURL]);

///////////

useEffect(() => {
  const decodeData = async () => {
    if (audioData) {
      const blob = new Blob([audioData]);

      // Create a new wavesurfer instance
      const wavesurfer = WaveSurfer.create({
         container: waveformRef.current,
        responsive: true,
        waveColor: '#4F4A85',
        progressColor: '#383351'
        
      });

      // Load the audio data from the fetched blob
      wavesurfer.loadBlob(blob);
    }
  }
  decodeData();

}, [audioData]);
  ////////


  ///////////
    return (
        <>
        
        <div>
 
      
      <audio ref={audioPlayerRef} controls type="audio/wav">
      </audio>
    </div>

    <div>
    <div ref={waveformRef} style={{width:"700px"}}></div>

    </div>
    {/* <div>
       <AudioHeatmap audioDataArrayBuffer={audioData} /> 
    </div> */}
        </>
    )
   
        
}

export default Spectrogram;