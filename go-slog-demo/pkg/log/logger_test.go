package log

import "testing"

func TestLogger_Infof(t *testing.T) {

}

func Test_global_log(t *testing.T) {
	Infof("Info msg %d %s", 1, "d")
	log := NewLogger(LevelInfo)
	log = log.WithName("test")
	log.Infof("info msg %d %s", 1, "d")
	zapLog := NewLoggerFromType(LevelInfo, ZapLoggerType)
	zapLog = zapLog.WithName("test")
	zapLog.Infof("info msg %d %s", 1, "d")
	zapLog.Errorf("test")
}
