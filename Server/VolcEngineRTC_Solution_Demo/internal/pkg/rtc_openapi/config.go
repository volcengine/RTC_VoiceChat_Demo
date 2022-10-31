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

import (
	"net/http"
	"net/url"
	"time"

	"github.com/volcengine/volc-sdk-golang/base"
)

const (
	DefaultRegion          = base.RegionCnNorth1
	ServiceVersion20201201 = "2020-12-01"
	ServiceName            = "rtc"
	ServiceHost            = "open.volcengineapi.com"
)

const (
	startRecord     = "StartRecord"
	updateRecord    = "UpdateRecord"
	sendUnicast     = "SendUnicast"     //房间外点对点消息
	sendRoomUnicast = "SendRoomUnicast" //房间内点对点消息
	sendBroadcast   = "SendBroadcast"   //房间内广播消息
)

var (
	serviceInfo = &base.ServiceInfo{
		Timeout: 5 * time.Second,
		Host:    ServiceHost,
		Header: http.Header{
			"Accept": []string{"application/json"},
		},
		Credentials: base.Credentials{Region: DefaultRegion, Service: ServiceName},
	}

	defaultApiInfoList = map[string]*base.ApiInfo{
		sendUnicast: {
			Method: http.MethodPost,
			Path:   "/",
			Query: url.Values{
				"Action":  []string{sendUnicast},
				"Version": []string{ServiceVersion20201201},
			},
		},
		sendRoomUnicast: {
			Method: http.MethodPost,
			Path:   "/",
			Query: url.Values{
				"Action":  []string{sendRoomUnicast},
				"Version": []string{ServiceVersion20201201},
			},
		},
		sendBroadcast: {
			Method: http.MethodPost,
			Path:   "/",
			Query: url.Values{
				"Action":  []string{sendBroadcast},
				"Version": []string{ServiceVersion20201201},
			},
		},
	}
)

type RTC struct {
	*base.Client
}

func NewInstance() *RTC {
	instance := &RTC{}
	instance.Client = base.NewClient(serviceInfo, defaultApiInfoList)
	return instance
}
