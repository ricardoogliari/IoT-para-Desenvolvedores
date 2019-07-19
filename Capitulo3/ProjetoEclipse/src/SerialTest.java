import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

public class SerialTest implements SerialPortEventListener {
	SerialPort serialPort;
	/** A porta que normalmente vamos usar */
	private static final String PORT_NAMES[] = {
			"/dev/cu.wchusbserial1410", // Mac OS X
			"/dev/ttyACM0", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	/**
	 * Um BufferedReader que será alimentado por um InputStreamReader
	 * convertendo os bytes em caracteres
	 */
	private BufferedReader input;
	/** O fluxo de saída para a porta */
	private OutputStream output;
	/** Milissegundos para bloquear enquanto aguarda a porta aberta */
	private static final int TIME_OUT = 2000;
	/** Bits por segundo para a porta COM. */
	private static final int DATA_RATE = 9600;
	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		// Primeiro, encontre uma instância da porta serial nomeada com
		// um dos valores do array PORT_NAMES
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId =
					(CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Não foi possível encontrar a porta COM");
			return;
		}
		try {
			// abrir a porta serial, e usar o nome da classe para o appName
			serialPort = (SerialPort) portId.open(
					this.getClass().getName(), TIME_OUT);
			// configurar os parâmetros da porta
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			// abrir os fluxos
			input = new BufferedReader(
					new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			// adicionar os ouvintes de eventos
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	/**
	 * Este método deve ser chamado quando a porta não é mais usada.
	 * Prevenindo bloqueios da porta em plataformas Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	/**
	 * Tratamento de um evento na porta serial. Lê e imprime o dado
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Todos os outros tipos de eventos foram ignorados neste exemplo
	}
	public static void main(String[] args) throws Exception {
		SerialTest main = new SerialTest();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				// a linha seguinte irá manter a aplicação viva por 1000 segundos,
				// esperando por eventos para responder a eles
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Iniciado");
	}
}