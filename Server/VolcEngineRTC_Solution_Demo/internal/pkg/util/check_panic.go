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
package util

import (
	"runtime"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs"
)

// const values
const (
	MaxStack = 5
)

// CheckPanic checks panics
func CheckPanic() {
	if err := recover(); err != nil {
		for i := 0; i <= MaxStack; i++ {
			if pc, file, line, ok := runtime.Caller(i); ok {
				f := runtime.FuncForPC(pc)
				logs.Error("panic error happened, stack:%d, med:%s, file:%s, line:%d, error:%s", i, f.Name(), file, line, err)
			}

		}
		logs.Error("panic error happened, error:%s", err)
	}
}
