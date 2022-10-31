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

type SvcSeat struct {
	RoomID      string `gorm:"column:room_id" json:"room_id"`
	SeatID      int    `gorm:"column:seat_id" json:"seat_id"` //1-8
	OwnerUserID string `gorm:"column:user_id" json:"user_id"` //申请中的不算，真正上麦的userID
	Status      int    `gorm:"column:status" json:"status"`   //0:lock  1:unlock
}
