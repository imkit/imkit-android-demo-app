package com.imkit;

import com.imkit.sdk.model.Room;

public class IIMKIT {

    public interface Login {

        void success();

        void failed(String reason);
    }

    public interface Logout {

        void done();
    }

    public interface UpdateUser {

        void success();

        void failed(String reason);
    }

    public interface CreateRoom {

        void success(String roomId, String title);

        void failed(String reason);
    }

    public interface CreateRoomInner {

        void success(Room room);

        void failed(String reason);
    }

    public interface RoomInfo {

        void success();

        void failed(String reason);
    }

    public interface Badge {

        void response(int count);
    }
}
