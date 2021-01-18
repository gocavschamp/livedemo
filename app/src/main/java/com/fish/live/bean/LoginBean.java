package com.fish.live.bean;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/18 16:19
 */
public class LoginBean {
        /**
         * SDKAppID
         */ /**
         * SDKAppID : 1400421744
         * userID : yuwenming
         * sig : eJwtzMEKgkAUheF3ma0hd6aZTKFFVkZhECq0Lpz0Ko5m1mTRu2fq8nwH-g*J-NB8ypo4hJlAJv3GWKoGr9hz*9BSFaiS8bzH*bmqMCYO5QCcUYvz4WmwkJ3OKNjCshkdVL4qrP8uhGAAMFYw6drZyqDGLQ3rULfrpRtNPS-b7d-zU3oJjFy5*hgctiIqN365IN8fNSQy3A__
         * roomID : 11100
         */

        private Integer SDKAppID;
        /**
         * userID
         */
        private String userID;
        /**
         * sig
         */
        private String sig;
        /**
         * roomID
         */
        private Integer roomID;

        public Integer getSDKAppID() {
            return SDKAppID;
        }

        public void setSDKAppID(Integer SDKAppID) {
            this.SDKAppID = SDKAppID;
        }

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getSig() {
            return sig;
        }

        public void setSig(String sig) {
            this.sig = sig;
        }

        public Integer getRoomID() {
            return roomID;
        }

        public void setRoomID(Integer roomID) {
            this.roomID = roomID;
        }
}
