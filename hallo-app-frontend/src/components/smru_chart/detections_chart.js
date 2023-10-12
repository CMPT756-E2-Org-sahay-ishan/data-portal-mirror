
import React, { useEffect, useState } from 'react';
import {
  Chart as ChartJS,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  TimeScale
} from 'chart.js';
import 'chartjs-adapter-date-fns';
import { Scatter } from 'react-chartjs-2';
ChartJS.register(LinearScale, PointElement, LineElement, Tooltip, Legend, TimeScale);

function SMRUDetectionsChart (props){
    // const data1=[{x:"2023-07-01", y:"12:51", idstring:'1q2w'},{x:"2023-07-03", y:"14:21",idstring:'azsx'}, {x:"2023-07-08", y:"08:30",idstring:'ikol'} ];
 
    const [idstrings, setIdstrings]=useState([]);
    const [currentIdstring, setCurrentIdstring]=useState("");
    const[chartData, setChartData]=useState([]);



var data={
    labels: props.data.map(function(e) {
        return e.date;}),
    datasets: [
   {
        label: 'SMRU Detections',
        data: props.data.map(function(e) {
            return new Date('01/01/2020 '+e.time).getHours() +new Date('01/01/2020 '+e.time).getMinutes()/60}),
        backgroundColor: 'rgba(255, 99, 132, 1)',
      },
     ],
 }

 props.data.map((item)=>{console.log(item.time)})

 var idStrings=props.data.map(function(e) {
    return e.idstring;})
///////////////
const options= {
    //To make it responsive
    //maintainAspectRatio: false,
   onClick: (event, elements, chart) => {
    if (elements[0]) {            
       const i = elements[0].index;
       return props.setStringidForParentFromChild(idStrings[i])

       //alert(props.id);
     
    }
  }
    ,scales: {
        x:{
            type: 'time', 
            time:{
                unit:'day'
            }
        },
        y:{
            min: 0,
            //max: 24,
            ticks: {
                // forces step size to be 50 units
                stepSize: 2,
                    // Include a dollar sign in the ticks
                    callback: function(value, index, ticks) {
                        if (value ===0) return "12 AM";
                        if (value<12) return  value+" AM";
                        if (value===12) return  "12 PM";
                        if (value>12 && value <24) return (value-12) +" PM";
                        if (value ===24) return "12 AM";
                    }
              }
    }


    },
    plugins: {
        tooltip: {
            callbacks: {
                label: function(context) {
                    let hours=Math.floor(context.parsed.y)
                    let minutes=Math.floor((context.parsed.y-hours)*60)
                    return hours +":"+minutes;
                }
            }
        }
    }
}

////////////////
    return  ( <Scatter options={options} data={data} />)

}

export default SMRUDetectionsChart;