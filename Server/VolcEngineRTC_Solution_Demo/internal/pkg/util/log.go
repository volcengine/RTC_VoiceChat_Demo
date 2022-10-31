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
	"context"

	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs/ctxvalues"
	"github.com/volcengine/VolcEngineRTC_Solution_Demo/internal/pkg/logs/logid"
)

// EnsureID ensures log id context
func EnsureID(ctx context.Context) context.Context {
	if len(RetrieveID(ctx)) == 0 {
		return SetID(ctx, logid.GenLogID())
	}
	return ctx
}

// SetID sets log id to context
func SetID(ctx context.Context, id string) context.Context {
	return ctxvalues.SetLogID(ctx, id)
}

// RetrieveID retrieves log if from context
func RetrieveID(ctx context.Context) string {
	logID, _ := ctxvalues.LogID(ctx)
	return logID
}
