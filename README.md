# Smart Parking System

To park in our parking system, all we need to do is scan our student ID (IITG) card. We’re alloted a slot which gets activated on a press of a button on our phone. Seamless locking system locks the cycle automatically as soon as the cycle is inserted in the alloted slot. The Intelligent anti-theft system ensures the safety of your cycle, so you don’t have to worry at all. And of course, the new smart parking provides support to your cycle, so situations like the one above, doesn’t arise.  


# Installation


## Install paho-mqtt

Install mosquitto to run python scripts for wireless communication

pip install paho-mqtt
sudo apt-get install mosquitto

Add this library of digital light sensor to arduino IDE.

## Connect the sensors to the Arduino mega 2560 as follows


1. Connect Base Sheild to the arduino.
2. Buzzer to A0
3. Digital light sensor to I2C
4. RFID
  1. +5 to Vcc of arduino board
  2. GND to GND of arduino
  3. TXD to RX1 of arduino
5. Laser KY08
  1. - to GND of arduino
  2. S to Pin 22 of arduino
  3. middle left unconnected.
6. Ultrasonic sensor
  1. GND to GND of arduino
  2. +5 to Vcc of arduino
  3. Trigger to 12
  4. Echo to 11
7. LCD display
8. Servo motor (Lock)
  1. Vcc to positive terminal of a battery 4.5V battery
  2. GND to negative terminal of battery
  3. servo pin to pin 6 of arduino
9. Connect WiFi adapter to raspi


## Follow the following steps to get the system running


- Upload final_ardm.ino on arduino Mega 2560
- Connect Arduino to Raspberry Pi
- Connect Pi to a WiFi network of your own
- SSH to the Raspberry Pi and 
- Run parking.py on Raspberry Pi
- Pi will be used as broker too. Hence IP of Pi needs to be changed in mqtt_publisher.py and mqtt_subsriber.py


## Hard Coded Values

We have created these scripts for just one slot. and two other demo slots created using LEDs simulating motor and laser on that port. To increase number of slots just connect correct sensors to remaining slots of arduino and add them to motor_port.txt, laser_port.txt and 

