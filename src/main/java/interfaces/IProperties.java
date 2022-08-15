package interfaces;

import enums.Protocol;

import java.util.Properties;

public interface IProperties {
    Properties getPropValues(Protocol protocol);
}
