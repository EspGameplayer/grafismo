import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeamStaffMember, getTeamStaffMemberIdentifier } from '../team-staff-member.model';

export type EntityResponseType = HttpResponse<ITeamStaffMember>;
export type EntityArrayResponseType = HttpResponse<ITeamStaffMember[]>;

@Injectable({ providedIn: 'root' })
export class TeamStaffMemberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/team-staff-members');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(teamStaffMember: ITeamStaffMember): Observable<EntityResponseType> {
    return this.http.post<ITeamStaffMember>(this.resourceUrl, teamStaffMember, { observe: 'response' });
  }

  update(teamStaffMember: ITeamStaffMember): Observable<EntityResponseType> {
    return this.http.put<ITeamStaffMember>(
      `${this.resourceUrl}/${getTeamStaffMemberIdentifier(teamStaffMember) as number}`,
      teamStaffMember,
      { observe: 'response' }
    );
  }

  partialUpdate(teamStaffMember: ITeamStaffMember): Observable<EntityResponseType> {
    return this.http.patch<ITeamStaffMember>(
      `${this.resourceUrl}/${getTeamStaffMemberIdentifier(teamStaffMember) as number}`,
      teamStaffMember,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeamStaffMember>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeamStaffMember[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTeamStaffMemberToCollectionIfMissing(
    teamStaffMemberCollection: ITeamStaffMember[],
    ...teamStaffMembersToCheck: (ITeamStaffMember | null | undefined)[]
  ): ITeamStaffMember[] {
    const teamStaffMembers: ITeamStaffMember[] = teamStaffMembersToCheck.filter(isPresent);
    if (teamStaffMembers.length > 0) {
      const teamStaffMemberCollectionIdentifiers = teamStaffMemberCollection.map(
        teamStaffMemberItem => getTeamStaffMemberIdentifier(teamStaffMemberItem)!
      );
      const teamStaffMembersToAdd = teamStaffMembers.filter(teamStaffMemberItem => {
        const teamStaffMemberIdentifier = getTeamStaffMemberIdentifier(teamStaffMemberItem);
        if (teamStaffMemberIdentifier == null || teamStaffMemberCollectionIdentifiers.includes(teamStaffMemberIdentifier)) {
          return false;
        }
        teamStaffMemberCollectionIdentifiers.push(teamStaffMemberIdentifier);
        return true;
      });
      return [...teamStaffMembersToAdd, ...teamStaffMemberCollection];
    }
    return teamStaffMemberCollection;
  }
}
