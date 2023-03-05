import { IVoteManager } from '@/shared/model/vote-manager.model';

import { VoteTargetType } from '@/shared/model/enumerations/vote-target-type.model';
import { VoteCcy } from '@/shared/model/enumerations/vote-ccy.model';
export interface IVoteTarget {
  id?: number;
  url?: string;
  votetype?: VoteTargetType;
  payout?: number;
  ccy?: VoteCcy;
  comment?: string | null;
  active?: boolean | null;
  funded?: boolean | null;
  created?: Date | null;
  expiry?: Date | null;
  boosted?: boolean | null;
  voteManager?: IVoteManager | null;
}

export class VoteTarget implements IVoteTarget {
  constructor(
    public id?: number,
    public url?: string,
    public votetype?: VoteTargetType,
    public payout?: number,
    public ccy?: VoteCcy,
    public comment?: string | null,
    public active?: boolean | null,
    public funded?: boolean | null,
    public created?: Date | null,
    public expiry?: Date | null,
    public boosted?: boolean | null,
    public voteManager?: IVoteManager | null
  ) {
    this.active = this.active ?? false;
    this.funded = this.funded ?? false;
    this.boosted = this.boosted ?? false;
  }
}
