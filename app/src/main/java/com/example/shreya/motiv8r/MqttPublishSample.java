package com.example.shreya.motiv8r;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttPublishSample implements MqttCallback {

    private MqttClient sampleClient = null;
    private MqttClient recieverClient = null;
    private MqttConnectOptions connOpts1 = null;
    private MqttConnectOptions connOpts2 = null;

    public MqttPublishSample() {

        String topic        = "battery";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        String broker       = "tcp://iot.eclipse.org:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            initializeAndConnectClients(broker, clientId, persistence);

            MqttMessage message = new MqttMessage(content.getBytes());
            setMessageCallbacks(qos, message);
            publishAndReceiveSubscription(topic, message);

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    private void setMessageCallbacks(int qos, MqttMessage message) {
        message.setQos(qos);
        recieverClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                Log.i("Subscription", "Subscription Received with message:" + s);
                System.out.println("Arrived");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

        });
    }

    private void initializeAndConnectClients(String broker, String clientId, MemoryPersistence persistence) throws MqttException {
        sampleClient = new MqttClient(broker, clientId, persistence);
        recieverClient = new MqttClient(broker, "Reciever", persistence);
        connOpts1 = new MqttConnectOptions();
        connOpts2 = new MqttConnectOptions();
        connOpts1.setCleanSession(true);
        connOpts2.setCleanSession(true);
        sampleClient.connect(connOpts1);
        recieverClient.connect(connOpts2);
    }

    private void publishAndReceiveSubscription(String topic, MqttMessage message) throws MqttException {
        recieverClient.subscribe(topic);
        sampleClient.publish(topic, message);
        System.out.println("Message published");
        sampleClient.disconnect();
        System.out.println("Disconnected");
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
