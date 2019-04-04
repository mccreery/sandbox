select
	Conversations.displayname, author,
	strftime('%d-%m-%Y', datetime(timestamp, unixepoch)) as timestamp,
	body_xml
from Messages
inner join Conversations on Conversations.id = Messages.convo_id
order by chatname, timestamp;
