import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBroadcastStaffMember, getBroadcastStaffMemberIdentifier } from '../broadcast-staff-member.model';

export type EntityResponseType = HttpResponse<IBroadcastStaffMember>;
export type EntityArrayResponseType = HttpResponse<IBroadcastStaffMember[]>;

@Injectable({ providedIn: 'root' })
export class BroadcastStaffMemberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/broadcast-staff-members');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(broadcastStaffMember: IBroadcastStaffMember): Observable<EntityResponseType> {
    return this.http.post<IBroadcastStaffMember>(this.resourceUrl, broadcastStaffMember, { observe: 'response' });
  }

  update(broadcastStaffMember: IBroadcastStaffMember): Observable<EntityResponseType> {
    return this.http.put<IBroadcastStaffMember>(
      `${this.resourceUrl}/${getBroadcastStaffMemberIdentifier(broadcastStaffMember) as number}`,
      broadcastStaffMember,
      { observe: 'response' }
    );
  }

  partialUpdate(broadcastStaffMember: IBroadcastStaffMember): Observable<EntityResponseType> {
    return this.http.patch<IBroadcastStaffMember>(
      `${this.resourceUrl}/${getBroadcastStaffMemberIdentifier(broadcastStaffMember) as number}`,
      broadcastStaffMember,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBroadcastStaffMember>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBroadcastStaffMember[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBroadcastStaffMemberToCollectionIfMissing(
    broadcastStaffMemberCollection: IBroadcastStaffMember[],
    ...broadcastStaffMembersToCheck: (IBroadcastStaffMember | null | undefined)[]
  ): IBroadcastStaffMember[] {
    const broadcastStaffMembers: IBroadcastStaffMember[] = broadcastStaffMembersToCheck.filter(isPresent);
    if (broadcastStaffMembers.length > 0) {
      const broadcastStaffMemberCollectionIdentifiers = broadcastStaffMemberCollection.map(
        broadcastStaffMemberItem => getBroadcastStaffMemberIdentifier(broadcastStaffMemberItem)!
      );
      const broadcastStaffMembersToAdd = broadcastStaffMembers.filter(broadcastStaffMemberItem => {
        const broadcastStaffMemberIdentifier = getBroadcastStaffMemberIdentifier(broadcastStaffMemberItem);
        if (broadcastStaffMemberIdentifier == null || broadcastStaffMemberCollectionIdentifiers.includes(broadcastStaffMemberIdentifier)) {
          return false;
        }
        broadcastStaffMemberCollectionIdentifiers.push(broadcastStaffMemberIdentifier);
        return true;
      });
      return [...broadcastStaffMembersToAdd, ...broadcastStaffMemberCollection];
    }
    return broadcastStaffMemberCollection;
  }
}
