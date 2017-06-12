#include "Timer.h"
#include <stdlib.h>
#include "MoteStatus.h"

module HomeSenseC
{

	uses {
		interface SplitControl as Control;
		interface Leds;
		interface Boot;
		interface Timer<TMilli> as PIRInput;
		interface HplMsp430GeneralIO as ADC6_PIN2;
		interface AMSend;
	}
}
implementation
{
	event void Boot.booted(){
		call Control.start();
	}

	event void Control.startDone(error_t err)
	{
		if (err == SUCCESS){
			call PIRInput.startPeriodic(1000);
			call ADC6_PIN2.makeInput();
		}
	}

	event void Control.stopDone(error_t err){}
	message_t reportMsg;
	bool sending=FALSE;	
	event void PIRInput.fired(){
		bool reading = call ADC6_PIN2.get();
		if(reading)
		{
			mote_status_msg_t *payload = call AMSend.getPayload(&reportMsg, sizeof(mote_status_msg_t));
			//call AMSend.getPayload(&reportMsg, sizeof(mote_status_msg_t));
			payload->status=2;
			call Leds.led2On();
			if(!sending){
				if(call AMSend.send(AM_BROADCAST_ADDR, &reportMsg, sizeof(mote_status_msg_t)) == SUCCESS){
            			sending = TRUE;	
				}else{
					call Leds.led0On();	
				}
			}

		}
		else{
			call Leds.led2Off();
		}		
	}

	event void AMSend.sendDone(message_t *msg, error_t error){
		if(error == SUCCESS){
			call Leds.led1On();
			call Leds.led0Off();
		}
		else{
			call Leds.led0On();
			call Leds.led1Off();
		}
		sending = FALSE;	
	}
}
