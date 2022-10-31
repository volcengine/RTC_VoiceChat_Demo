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
package svc_entity

import "time"

type SvcRoom struct {
	ID                          int64     `gorm:"column:id" json:"id"`
	AppID                       string    `gorm:"column:app_id" json:"app_id"`
	RoomID                      string    `gorm:"column:room_id" json:"room_id"`
	RoomName                    string    `gorm:"column:room_name" json:"room_name"`
	HostUserID                  string    `gorm:"column:host_user_id" json:"host_user_id"`
	HostUserName                string    `gorm:"column:host_user_name" json:"host_user_name"`
	Status                      int       `gorm:"column:status" json:"status"`
	EnableAudienceInteractApply int       `gorm:"column:enable_audience_interact_apply" json:"enable_audience_interact_apply"` //0:false 1:true
	CreateTime                  time.Time `gorm:"column:create_time" json:"create_time"`
	UpdateTime                  time.Time `gorm:"column:update_time" json:"update_time"`
	StartTime                   int64     `gorm:"column:start_time" json:"start_time"`
	FinishTime                  int64     `gorm:"column:finish_time" json:"finish_time"`
	Ext                         string    `gorm:"column:ext" json:"ext"`
}

type SvcRoomExt struct {
	BackgroundImageName string `json:"background_image_name"`
}
