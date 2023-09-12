package logs

import (
	"golang.org/x/exp/slog"
	"testing"
)

func Test_Quick_Log(t *testing.T) {
	slog.Info("Info Message", "account", "admin")
	slog.Warn("Warn Message")
	slog.Debug("Error Debug")
	slog.Error("Error Message")
}
