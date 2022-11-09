/*
 * Copyright 2022 CloudWeGo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package api

const (
	EventTypeUserLeaveRoom = "UserLeaveRoom"
)

const (
	LeaveRoomReasonUserLeave      = "userLeave"
	LeaveRoomReasonConnectionLost = "connectionLost"
)

type RtmParam struct {
	Message string `json:"message"`
}

type RtmCallbackParam struct {
	EventType string `json:"EventType"`
	EventData string `json:"EventData"`
	EventTime string `json:"EventTime"`
	EventId   string `json:"EventId"`
	AppId     string `json:"AppId"`
	Version   string `json:"Version"`
	Signature string `json:"Signature"`
	Nonce     string `json:"Nonce"`
}

type EventDataLeaveRoom struct {
	RoomId     string `json:"RoomId"`
	UserId     string `json:"UserId"`
	DeviceType string `json:"DeviceType"`
	Reason     string `json:"Reason"`
	Timestamp  int64  `json:"Timestamp"`
}
