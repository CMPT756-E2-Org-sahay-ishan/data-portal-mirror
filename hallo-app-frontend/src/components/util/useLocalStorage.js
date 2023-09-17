import { useEffect, useState } from "react"

function useLocalState(defaultValue, key){
    
   const [value, setValue]= useState(()=>{
        const localStorageValue=localStorage.getItem(key);
       return localStorageValue !=null? JSON.parse(localStorageValue):defaultValue
    });
    //console.log(`JWT is: ${key} is ${value}`)
    useEffect(()=>{
        localStorage.setItem(key, JSON.stringify(value))
    }, [key, value])
    //console.log(`JWT is: ${key} is ${value}`)
    return [value, setValue]
}



export {useLocalState}