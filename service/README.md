# org.eevolution.service
Service Management This functionality allows the management of contracts, recurring billing, better integration of request management, expense and project management.

Install Coursier https://get-coursier.io/docs/cli-installation

```
cs install --channel https://raw.githubusercontent.com/oyvindberg/bleep/master/coursier-channel.json bleep
bleep compile
bleep dist org.eevolution.service-management
ls .bleep/builds/normal/.bloop/org.eevolution.service-management/dist/lib
mkdir -p $ADEMPIERE_HOME/package/ServiceManagement/lib
cp -R .bloop/org.eevolution.service-management/dist/lib/org.eevolution.service-management.jar  $ADEMPIERE_HOME/package/ServiceManagement/lib
```