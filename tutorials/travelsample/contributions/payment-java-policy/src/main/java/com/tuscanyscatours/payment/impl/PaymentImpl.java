/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.tuscanyscatours.payment.impl;

import org.oasisopen.sca.annotation.Property;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Requires;
import org.oasisopen.sca.annotation.Service;

import com.tuscanyscatours.customer.Customer;
import com.tuscanyscatours.customer.CustomerNotFoundException;
import com.tuscanyscatours.customer.CustomerRegistry;
import com.tuscanyscatours.emailgateway.EmailGateway;
import com.tuscanyscatours.payment.Payment;
import com.tuscanyscatours.payment.creditcard.AuthorizeFault_Exception;
import com.tuscanyscatours.payment.creditcard.CreditCardPayment;

/**
 * The payment implementation
 */
@Service(Payment.class)
//@RolesAllowed({"Admin", "Billing"})
//@RunAs("Billing")
public class PaymentImpl implements Payment {

    @Reference
    protected CustomerRegistry customerRegistry;

    @Reference
    //@Authentication - not supported
    @Requires("{http://docs.oasis-open.org/ns/opencsa/sca/200912}authentication")
    protected CreditCardPayment creditCardPayment;

    @Reference
    protected EmailGateway emailGateway;

    @Property
    protected float transactionFee = 0.01f;

    public String makePaymentMember(String customerId, float amount) {
        try {
            Customer customer = customerRegistry.getCustomer(customerId);
            String status = creditCardPayment.authorize(customer.getCreditCard(), amount + transactionFee);
            emailGateway.sendEmail("order@tuscanyscatours.com",
                                   customer.getEmail(),
                                   "Status for your payment",
                                   customer + " >>> Status = " + status);
            return status;
        } catch (CustomerNotFoundException ex) {
            return "Payment failed due to " + ex.getMessage();
        } catch (AuthorizeFault_Exception e) {
            return e.getFaultInfo().getErrorCode();
        } catch (Throwable t) {
            return "Payment failed due to system error " + t.getMessage();
        }
    }
}