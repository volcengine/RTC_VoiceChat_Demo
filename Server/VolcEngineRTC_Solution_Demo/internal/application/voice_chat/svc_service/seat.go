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
package svc_service

import (
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/application/voice_chat/svc_entity"
)

type Seat struct {
	*svc_entity.SvcSeat
	isDirty bool
}

func (s *Seat) IsDirty() bool {
	return s.isDirty
}

func (s *Seat) GetRoomID() string {
	return s.RoomID
}

func (s *Seat) GetSeatID() int {
	return s.SeatID
}

func (s *Seat) GetOwnerUserID() string {
	return s.OwnerUserID
}

func (s *Seat) IsEnableAlloc() bool {
	return !s.IsLock() && s.OwnerUserID == ""
}

func (s *Seat) IsLock() bool {
	return s.Status == 0
}

func (s *Seat) SetIsDirty(status bool) {
	s.isDirty = status
}

func (s *Seat) Lock() {
	s.Status = 0
	s.isDirty = true
}

func (s *Seat) Unlock() {
	s.Status = 1
	s.isDirty = true
}

func (s *Seat) SetOwnerUserID(userID string) {
	s.OwnerUserID = userID
	s.isDirty = true
}
