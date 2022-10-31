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
package ctxvalues

import "context"

const CtxLogID = "LogID"

func SetLogID(ctx context.Context, logID string) context.Context {
	return context.WithValue(ctx, CtxLogID, logID)
}

func LogID(ctx context.Context) (string, bool) {
	return getStringFromContext(ctx, CtxLogID)
}

func getStringFromContext(ctx context.Context, key string) (string, bool) {
	if ctx == nil {
		return "", false
	}

	v := ctx.Value(key)
	if v == nil {
		return "", false
	}

	switch v := v.(type) {
	case string:
		return v, true
	case *string:
		if v == nil {
			return "", false
		}
		return *v, true
	}
	return "", false
}
