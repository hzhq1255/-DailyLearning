package log

import (
	"fmt"
	"golang.org/x/exp/slog"
	"os"
	"testing"
)

func Test_Quick_Log(t *testing.T) {
	opts := &slog.HandlerOptions{AddSource: true, Level: slog.LevelInfo}
	l := slog.New(slog.NewJSONHandler(os.Stdout, opts))
	l = l.WithGroup("aaaa")
	l.Info("Info Message", "account", "admin")
	l.Warn("Warn Message")
	l.Debug("Error Debug")
	l.Error("Error Message")
}

func Test_append_file(t *testing.T) {
	log := newSlogLogger(LevelInfo, true)
	log.WithName("test")
	log.Infow("Info message", "a", "b")
	log.Infof("Info message %s ", "a")
}

func Test_zap(t *testing.T) {
	log := newZapLogger(LevelInfo)
	log.Infow("Info message", "a", "b")
	log.Errorf("Error message %v", fmt.Errorf("error1"))

}
