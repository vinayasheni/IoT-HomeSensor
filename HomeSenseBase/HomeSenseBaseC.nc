#include "Timer.h"
#include "MoteStatus.h"
#include <stdlib.h>

module HomeSenseBaseC
{

	uses{
		interface SplitControl as Control;
		interface Leds;
		interface Boot;
		interface Timer<TMilli> as PIRInput;
		interface Packet;
		interface Receive;
		interface AMSend;
	}
}

implementation
{
	event void Boot.booted(){
		call Control.start();
	}

        event void Control.startDone(error_t err){
		if(err == SUCCESS){
			call PIRInput.startPeriodic(1000);
		}
	}
	event  void Control.stopDone(error_t err){}

	event void PIRInput.fired(){
	}

	event message_t* Receive.receive(message_t* bufPtr,
                                   void* payload, uint8_t len) {

             call Leds.led2On();
	     if(call AMSend.send(AM_BROADCAST_ADDR, bufPtr, sizeof(mote_status_msg_t)) == SUCCESS){
		//call Leds.led2On();	
		}else{
		//call Leds.led0On();
	} 
	     return bufPtr;	
	}

	event void AMSend.sendDone(message_t* bufPtr, error_t error) {
	if (error == SUCCESS) {
        call Leds.led1On(); 
    }else{
	call Leds.led0On();
	}
  }


}

