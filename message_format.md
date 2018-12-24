
# Sending Message Format
| Field       | Type   | Description |
| -------     | ------ | ----------- |
| message     | String | Text message content |
| messageType | String | Custom message type |
| room        | String | Room ID |
| originalUrl | String | (optional) Media URL |
| thumbnailUrl | String | (optional) Thumbnail URL |
| width       | Number | (optional) Image or video width |
| height      | Number | (optional) Image or video height |
| duration    | Number | (optional) Video or audio length |
| latitude    | Number | (optional) Location latitude |
| longitude   | Number | (optional) Location longitude |
| reply       | String | (optional) a Message ID that current message replies to |
| sticker     | String | (optional) Sticker ID |
| extra       | String | (optional) encoded json string |

```javascript
var message = {};
//message['message'] = 'text message content';
message['message'] = {'customProperty': 'custom content'};
message['room'] = $('#room').val();
socket.emit('chat message', message, function(ack) {
  if (ack && ack.RC !== 0) {
    console.error(ack);
  } else {
    console.log(ack);
  }
});
```
# Predefined Message Types
| MessageType  |
| ------------ |
| text         |
| image        |
| video        |
| audio        |
| location     |
| sticker      |
| url          |
| file         |
| joinRoom     |
| leaveRoom    |
| addMember    |
| addMembers   |
| deleteMember |


```javascript
const messageTypes = {
  text: 'text',
  joinRoom: 'joinRoom',
  leaveRoom: 'leaveRoom',
  addMember: 'addMember',
  deleteMember: 'deleteMember',
};
```

### Text
```javascript
{
  "_id": "58bec67de0eeac088c70ef04",
  "message": "aaaaaa",
  "messageType": "text",
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:41:01.934Z",
  "messageTimeMS": 1488897661934,
  "id": "58bec67de0eeac088c70ef04"
}
```

### URL
```javascript
{
  "_id": "58bec67de0eeac088c70ef04",
  "message": "aaaaaa",
  "messageType": "url",
  "url": "http://example.com",
  "extra": {
     "title": "web site title",
     "description": "web description",
     "images": "preview image url"
  },
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:41:01.934Z",
  "messageTimeMS": 1488897661934,
  "id": "58bec67de0eeac088c70ef04"
}
```

### File
```javascript
{
  "_id": "58bec67de0eeac088c70ef04",
  "messageType": "file",
  "url": "file-url",
  "extra": {
     "title": "File display name",
     "mimetype": "File mime-type"
  },
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:41:01.934Z",
  "messageTimeMS": 1488897661934,
  "id": "58bec67de0eeac088c70ef04"
}
```

### Join Room
```javascript
{
  "_id": "58bec8fc63a60808a2522be0",
  "messageType": "joinRoom",
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:51:40.522Z",
  "messageTimeMS": 1488898300522,
  "id": "58bec8fc63a60808a2522be0"
}
```
### Leave Room
```javascript
{
  "_id": "58bec8ec63a60808a2522bdf",
  "messageType": "leaveRoom",
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:51:24.386Z",
  "messageTimeMS": 1488898284386,
  "id": "58bec8ec63a60808a2522bdf"
},
```

### Add Member
```javascript
{
  "_id": "58bec8a163a60808a2522bdd",
  "messageType": "addMember",
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "member": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:50:09.476Z",
  "messageTimeMS": 1488898209476,
  "id": "58bec8a163a60808a2522bdd"
}
```

### Add Members
```javascript
{
      "_id": "5bdc37fd87d67f1812b31944",
      "messageType": "addMembers",
      "sender": {
        "_id": "bossiniHK",
        "nickname": "Lara",
        "lastLoginTimeMS": 0,
        "id": "bossiniHK"
      },
      "message": "[{\"_id\":\"aaa\",\"nickname\":\"Gretchen\",\"lastLoginTimeMS\":0,\"id\":\"aaa\"},{\"_id\":\"bbb\",\"nickname\":\"Calista\",\"lastLoginTimeMS\":0,\"id\":\"bbb\"},{\"_id\":\"ccc\",\"nickname\":\"Aurora\",\"lastLoginTimeMS\":0,\"id\":\"ccc\"}]",
      "messageTime": "2018-11-02T11:41:49.504Z",
      "messageTimeMS": 1541158909504,
      "id": "5bdc37fd87d67f1812b31944"
}
```

### Delete Member
```javascript
{
  "_id": "58bec8c363a60808a2522bde",
  "messageType": "deleteMember",
  "sender": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "member": {
    "_id": "1485248560558",
    "nickname": "Test AB",
    "avatarUrl": "http://example.com/avatarUrl",
    "lastLoginTime": "2017-03-07T14:40:57.470Z",
    "lastLoginTimeMS": 1488897657470,
    "id": "1485248560558"
  },
  "messageTime": "2017-03-07T14:50:43.492Z",
  "messageTimeMS": 1488898243492,
  "id": "58bec8c363a60808a2522bde"
}
```
