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
package response

import (
	"encoding/json"
	"time"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

type inform struct {
	MessageType string      `json:"message_type"`
	Event       string      `json:"event"`
	Timestamp   int64       `json:"timestamp"`
	Data        interface{} `json:"data"`
}

func NewInformToClient(event string, data interface{}) string {
	info := inform{
		MessageType: "inform",
		Event:       event,
		Timestamp:   time.Now().UnixNano(),
		Data:        data,
	}

	infoByte, err := json.Marshal(info)
	if err != nil {
		logs.Warnf("json marshal error, input: %v, err: %v", info, err)
	}

	return string(infoByte)
}
