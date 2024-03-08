import React, { useEffect, useRef } from 'react';

const AudioHeatmap = ({ audioDataArrayBuffer }) => {
  const canvasRef = useRef(null);

  useEffect(() => {
    if (!audioDataArrayBuffer || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');

    const audioData = new Float32Array(audioDataArrayBuffer);

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const barWidth = canvas.width / audioData.length;
    const barHeightScale = canvas.height;

    audioData.forEach((intensity, index) => {
      const x = index * barWidth;
      const y = (1 - intensity) * barHeightScale;
      const barHeight = canvas.height - y;

      ctx.fillStyle = `rgba(255, 0, 0, ${intensity})`; // Red color
      ctx.fillRect(x, y, barWidth, barHeight);
    });
  }, [audioDataArrayBuffer]);

  return (
    <canvas
      ref={canvasRef}
      width={800}
      height={300}
      style={{ border: '1px solid #000' }}
    ></canvas>
  );
};

export default AudioHeatmap;

