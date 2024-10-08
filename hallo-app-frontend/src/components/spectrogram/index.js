import React, { useEffect, useRef, useState } from 'react';
import { useLocalState } from '../util/useLocalStorage';
import WaveSurfer from 'wavesurfer.js';
import { default as wsSprectrogram } from 'wavesurfer.js/dist/plugins/spectrogram.js';
import { default as wsTimelinePlugin } from 'wavesurfer.js/dist/plugins/timeline.js';
import Spinner from "react-spinners/CircleLoader";
function Spectrogram({ apiUrl }) {
  const [jwt, setJwt] = useLocalState('', 'jwt');
  const waveformRef = useRef(null);
  const [loading, setLoading] = useState(true); // State variable to track loading

  // Create a state variable for Wavesurfer
  const [wavesurfer, setWavesurfer] = useState(null);

  useEffect(() => {
    // Destroy the current Wavesurfer instance if it exists
    if (wavesurfer) {
      wavesurfer.destroy();
    }
    //Always the loading is set to be true when the page is rerednered 
    setLoading(true);
    // Create a new Wavesurfer instance
    const newWavesurfer = WaveSurfer.create({
      container: waveformRef.current,
      responsive: true,
       
      normalize: true,  /** Stretch the waveform to the full height */
      waveColor: '#ff4e00',
      progressColor: '#dd5e98',
      mediaControls: true,
      interact: true, /** Pass false to disable clicks on the waveform */
      dragToSeek: true,  /** Allow to drag the cursor to seek to a new position */
    });

    const colormap = require('colormap');
    const colors = colormap({
      colormap: 'hot',
      nshades: 256,
      format: 'float'
    });


    // Initialize the Spectrogram plugin
    newWavesurfer.registerPlugin(
      wsSprectrogram.create({
        labels: true,
        height: 200,
        colorMap: colors,
    
      })
    );

    // Initialize the Timeline plugin
    newWavesurfer.registerPlugin(wsTimelinePlugin.create());

    // Load audio data from the fetched URL
    fetchAudio(newWavesurfer, apiUrl);
    setWavesurfer(newWavesurfer); // Save the constructed new Wavesurfer instance in the wavesurfer instance
  }, [apiUrl]);

  // Fetch audio data and load it into Wavesurfer
  const fetchAudio = async (ws, audioUrl) => {
    try {
      const response = await fetch(audioUrl, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${jwt}`,
        },
        method: 'GET',
      });
      const data = await response.arrayBuffer();
      ws.loadBlob(new Blob([data]));

      setLoading(false); // Set loading to false 

    } catch (error) {
      console.error('Error fetching audio:', error);
      setLoading(false); // Set loading to false

    }
  };

  return (    
  <div>
    <div style={{width:'20%', margin:'auto'}}>

      {loading ? (
          <div>
            <Spinner />
            <p>Loading</p>
          </div>
        ) : null}
    </div>

    <div ref={waveformRef}></div>
  </div>)
}

export default Spectrogram;
