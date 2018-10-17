@Grab(group='com.quali.cloudshell', module='sandbox-api', version='1.1.0.14' )
import com.quali.cloudshell.qsExceptions.SandboxApiException 
def exampleMethod() {
    sandboxId = startSandbox(maxDuration: 30, name: 'test conflict', sandboxName: 'Flex - Test - HA_' + build_number, timeout: 1 )

    //do something
}

def otherExampleMethod() {
    //do something else
}
return this