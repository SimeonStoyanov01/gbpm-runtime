package cs.rug.gbpmruntime.engineintegration.infrastructure.camunda8.worker;

public record DemoTransportResourceProfile(
        String resourceId,
        String resourceType,
        String energyModelType,
        double ratedPowerKw,
        double idlePowerKw,
        double defaultLoadFactor,
        double startupEnergyKwh
) {

    public static DemoTransportResourceProfile transportUnit() {
        return new DemoTransportResourceProfile(
                "TRANSPORT_UNIT_01",
                "transport-unit",
                "POWER_DURATION",
                18.0,
                2.5,
                0.65,
                0.4
        );
    }
}
