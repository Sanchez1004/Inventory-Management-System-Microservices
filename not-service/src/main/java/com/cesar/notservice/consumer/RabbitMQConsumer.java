package com.cesar.notservice.consumer;

import com.cesar.notservice.dto.InventoryDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * The {@code RabbitMQConsumer} class is a Spring service that listens to RabbitMQ message queues
 * and processes messages received from those queues. This class demonstrates a basic implementation
 * of RabbitMQ message consumption and processing, and can be customized to fit specific needs or use cases.
 *
 * <p>This implementation serves as an example of how to handle RabbitMQ messages and includes basic
 * processing for different message types. It can be modified to support additional message types,
 * integrate with other notification systems (such as SMS, Teams, email, etc.), and handle more complex
 * business logic as required.</p>
 *
 * <p>The class listens to two types of queues:</p>
 * <ul>
 *     <li><b>String Queue:</b> A queue that receives messages as plain strings. This example logs the
 *     received message.</li>
 *     <li><b>JSON Queue:</b> A queue that receives messages in JSON format. The JSON messages are
 *     deserialized into {@link InventoryDTO} objects, and processing is based on the category of the
 *     inventory item.</li>
 * </ul>
 *
 * <p>Note that the handling of different inventory categories is implemented in a basic manner. The
 * example methods, {@code itemUpdateResponse} and {@code lowStockResponse}, only log the received
 * objects. These methods can be modified to include additional processing logic or integrate with
 * other systems as needed.</p>
 *
 * <p>This class is intended for demonstration purposes and may need to be adjusted according to the
 * specific requirements of your application. For example, you might need to add error handling,
 * configure additional queues, or implement more complex processing logic.</p>
 *
 * @see InventoryDTO
 */
@Service
public class RabbitMQConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@code RabbitMQConsumer} with the specified {@code ObjectMapper}.
     *
     * @param objectMapper the {@code ObjectMapper} used for JSON deserialization
     */
    public RabbitMQConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Listens to the queue specified by the {@code rabbitmq.inventory.queue.string.name} property and
     * processes incoming messages as plain strings. This method logs the received message.
     *
     * <p>This method can be customized to perform additional actions based on the received string message,
     * such as integrating with other systems or performing specific business logic.</p>
     *
     * @param message the message received from the queue
     */
    @RabbitListener(queues = {"${rabbitmq.inventory.queue.string.name}"})
    public void consumeInventoryStringQueue(String message) {
        logger.info("Received message -> {}", message);
    }

    /**
     * Listens to the queue specified by the {@code rabbitmq.inventory.queue.json.name} property and
     * processes incoming messages as JSON. The JSON message is deserialized into an {@link InventoryDTO}
     * object, and the processing logic is based on the inventory category.
     *
     * <p>This method demonstrates basic handling for different inventory categories, such as low stock
     * and item updates. The handling logic can be extended or modified to fit specific needs, including
     * integrating with other notification systems or implementing additional business logic.</p>
     *
     * @param jsonMessage the JSON message received from the queue
     * @throws JsonProcessingException if there is an error processing the JSON message
     */
    @RabbitListener(queues = {"${rabbitmq.inventory.queue.json.name}"})
    public void consumeInventoryJSONQueue(String jsonMessage) throws JsonProcessingException {
        InventoryDTO inventoryDTO = objectMapper.readValue(jsonMessage, InventoryDTO.class);
        logger.info("Received object -> {}", inventoryDTO);

        switch (inventoryDTO.getHandleCategory()) {
            case LOW_STOCK:
                lowStockResponse(inventoryDTO);
                break;
            case ITEM_UPDATED:
                itemUpdateResponse(inventoryDTO);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + inventoryDTO.getHandleCategory());
        }
    }

    /**
     * Handles the processing of an {@link InventoryDTO} object when the inventory category is
     * {@code ITEM_UPDATED}. This method currently logs the received object but can be modified
     * to perform additional actions, such as updating a database or sending notifications.
     *
     * @param inventoryDTO the {@code InventoryDTO} object to process
     */
    private void itemUpdateResponse(InventoryDTO inventoryDTO) {
        logger.info("item update received object -> {}", inventoryDTO);
    }

    /**
     * Handles the processing of an {@link InventoryDTO} object when the inventory category is
     * {@code LOW_STOCK}. This method logs the received object and includes basic logic to check
     * the quantity of the item. It can be customized to perform additional actions, such as sending
     * notifications or triggering alerts based on the stock level.
     *
     * @param inventoryDTO the {@code InventoryDTO} object to process
     */
    private void lowStockResponse(InventoryDTO inventoryDTO) {
        if (inventoryDTO.getItem().getQuantity() > 0) {
            logger.info("low stock received object -> {}", inventoryDTO);
        } else {
            logger.info("No stock received object -> {}", inventoryDTO);
        }
    }
}
