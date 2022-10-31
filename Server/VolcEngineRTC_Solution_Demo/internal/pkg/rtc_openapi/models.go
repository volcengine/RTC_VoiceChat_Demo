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
package rtc_openapi

type sendUnicastParam struct {
	AppID   string `json:"AppId"`
	From    string `json:"From"`
	To      string `json:"To"`
	Binary  bool   `json:"Binary"`
	Message string `json:"Message"`
}

type sendRoomUnicastParam struct {
	AppID   string `json:"AppId"`
	RoomID  string `json:"RoomId"`
	From    string `json:"From"`
	To      string `json:"To"`
	Binary  bool   `json:"Binary"`
	Message string `json:"Message"`
}

type sendBroadcastParam struct {
	AppID   string `json:"AppId"`
	RoomID  string `json:"RoomId"`
	From    string `json:"From"`
	Binary  bool   `json:"Binary"`
	Message string `json:"Message"`
}
