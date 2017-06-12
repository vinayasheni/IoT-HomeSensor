#ifndef MOTE_STATUS_h
#define MOTE_STATUS_h

typedef nx_struct mote_status_msg {
  nx_uint16_t status;
} mote_status_msg_t;

enum {
   AM_MOTE_STATUS_MSG = 0x89,
 
};




#endif
            
