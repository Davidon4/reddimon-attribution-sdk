declare module '@reddimon/react-native-attribution' {
  export interface AttributionConfig {
    publisherId: string;
    appId: string;
    baseUrl: string;
    apiKey: string;
    enableDebugLogs?: boolean;
    connectionTimeoutSeconds?: number;
    readTimeoutSeconds?: number;
    retryCount?: number;
  }

  export interface InstallationEvent {
    referrerUrl?: string;
    installTime: number;
    clickTime?: number;
    campaignId?: string;
  }

  export interface SubscriptionEvent {
    subscriptionId: string;
    subscriptionType: string;
    price: number;
    currency: string;
    startDate: number;
    endDate?: number;
    trialPeriod?: boolean;
  }

  export interface ActivationEvent {
    isFirstLaunch: boolean;
    sessionId?: string;
    customData?: Record<string, string | number | boolean>;
  }

  export type EventType = 'installation' | 'subscription' | 'activation';

  export interface Attribution {
    initialize(config: AttributionConfig): Promise<void>;
    trackEvent(
      eventType: EventType,
      eventData: InstallationEvent | SubscriptionEvent | ActivationEvent
    ): Promise<void>;
  }

  const Attribution: Attribution;
  export default Attribution;
} 