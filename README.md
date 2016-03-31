# Low Battery Notifier App
Nowadays, it is common for one to rely on their friends and family always being reachable by phone.
When someone is unreachable for a few hours, people start to worry that something has happened to them.
So, I made an app that texts selected contacts when your phone is under a specified battery life. 
When it sends out the texts, the user receives a notification saying that the contacts have been notified of low battery life. 

## How it works: 
1. User installs app and selects contacts to be notified when phone is low on battery.
contacts stored in shared preferences string set.
2. Service registers a broadcast receiver that listens for “On Changed Battery Events”
3. If sending texts is enabled:
	onReceive function:
      If text has not yet been sent out for current cycle (textSent in sharedPreferences is false), 
      and phone is less than 5% battery life:
          Text is sent out to set of contacts,
          Notification is issued
          boolean TextSent is set to True. 
      When phone is back up above 20% & phone is charging, TextSent will be be reset to False so 
      next time it falls below 5% another text and notification will be sent.
