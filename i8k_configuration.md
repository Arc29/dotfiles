# Dell Inspiron 5593 fan control setup

Researched from:
1. [https://www.cyberciti.biz/faq/controlling-dell-fan-speeds-temperature-on-ubuntu-debian-linux/](https://www.cyberciti.biz/faq/controlling-dell-fan-speeds-temperature-on-ubuntu-debian-linux/)
2. [http://manpages.ubuntu.com/manpages//precise/man1/i8kmon.1.html](http://manpages.ubuntu.com/manpages//precise/man1/i8kmon.1.html)
3. [https://askubuntu.com/questions/522954/setting-temperature-thresholds-in-i8kmon-ubuntu-14-04](https://askubuntu.com/questions/522954/setting-temperature-thresholds-in-i8kmon-ubuntu-14-04)
4. [https://askubuntu.com/questions/1227489/i8kctl-not-working-for-dell-inspiron-fan-control](https://askubuntu.com/questions/1227489/i8kctl-not-working-for-dell-inspiron-fan-control).

--------------------

## Steps:

1. `sudo apt install lm-sensors`, it will allow us to monitor temperature of cpu and fan speeds via `sensors` command.

2. `sudo apt install i8kutils`, this has i8k, the driver and utility which will help us control the fan.

3. `sudo modprobe -v i8k`, this will load i8k driver.

4. Then follow:
	```
	git clone https://github.com/TomFreudenberg/dell-bios-fan-control.git
	cd dell-bios-fan-control
	make
	sudo ./dell-bios-fan-control 0
	```
  
  	to disable BIOS control of fans.
  
  	If you get an error like this
	```
	ioperm:: Operation not permitted 
	ioperm:: Operation not permitted [1] 
	4701 segmentation fault
	```
	you need to go to your BIOS/UEFI and disable SECURE BOOT. Then run the command again. Output should now be `BIOS CONTROL DISABLED`.

5. If everything has worked till now
	```
	sudo i8kctl fan 2 2
	```
	should put your fans at full speed.

6. Now open `/etc/i8kmon.conf` in a text editor
	```
	sudo nano /etc/i8kmon.conf
	```
	and put the following in it:
	```
	# Run as daemon, override with --daemon option
	set config(daemon)      0

	# Automatic fan control, override with --auto option
	set config(auto)        1

	# External program to control the fans
	set config(i8kfan)	/usr/bin/i8kfan

	# Report status on stdout, override with --verbose option
	set config(verbose)	1

	# Status check timeout (seconds), override with --timeout option
	set config(timeout)	5

	# Temperature threshold at which the temperature is displayed in red
	set config(t_high)	80

	# Temperature thresholds: {fan_speeds low_ac high_ac low_batt high_batt}
	# These were tested on the Inspiron 5593. If you have a different Dell laptop model
	# you should check the BIOS temperature monitoring and set the appropriate
	# thresholds here. In doubt start with low values and gradually rise them
	# until the fans are not always on when the cpu is idle.
	set config(0)   { {-1 0}  -1  40  -1  40 }
	set config(1)   { {-1 1}  40  60  40  60 }
	set config(2)   { {-1 2}  60  128  60  128 }
	set config(3)   { {-1 2}  60  128  60  128 }

	# Speed values are set here to avoid i8kmon probe them at every time it starts.
	set status(leftspeed)	"0 1000 2000 3000"
	set status(rightspeed)	"0 1000 2000 3000"

	# end of file
	```

	Remember to replace those {-1 0} things with specific to your laptop. -1 signifies my laptop doesn't have a left fan. If you have both fans put {0 0}, if only right {0 -1}. Refer manpage for more details.

7. Create `/etc/modprobe.d/i8k.conf` with this line inside it:
	```
	options i8k force=1
	```

8. Thats it. Reboot your system and enjoy a cooler laptop.

<br>

----------------------

To check status of i8kmon service:
```
sudo systemctl status i8kmon.service
```
To start it:
```
sudo systemctl start i8kmon.service
```
To stop it:
```
sudo systemctl stop i8kmon.service
```
To restart it:
```
sudo systemctl restart i8kmon.service
```
----------------------

To heat CPU cores for checking i8k fan control you can do a stress test using:
```
stress -c 8
```
and monitor temperatures using:
```
watch sensors
```
and press CTRL+c to cancel when satisfied.

----------------------
