import com.soprabanking.dxp.*
import org.springframework.cloud.contract.verifier.plugin.ContractVerifierExtension

dxpKotlinApplication()
dependencies {
	implementation(dxp("api"))
	implementation(dxp("client"))
	runtimeOnly(dxp("monitor"))
	testImplementation(dxp("test"))
    
}

tasks.withType<Test>().configureEach { failFast = false }
